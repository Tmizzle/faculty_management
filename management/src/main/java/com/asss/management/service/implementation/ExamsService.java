package com.asss.management.service.implementation;

import com.asss.management.dao.*;
import com.asss.management.entity.*;
import com.asss.management.entity.Enums.Currency;
import com.asss.management.entity.Enums.Finances_status;
import com.asss.management.entity.Enums.Type_of_event;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.ExamsDTO;
import com.asss.management.service.mapper.ExamsMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Data
public class ExamsService {

    private final ExamsRepo examsRepo;
    private final ExamsMapper examsMapper;
    private final JwtService jwtService;
    private final EventsRepo eventsRepo;
    private final StudentRepo studentRepo;
    private final EmployeeRepo employeeRepo;
    private final SubjectsRepo subjectsRepo;
    private final FinancesRepo financesRepo;

    // Retrieves all exams registered
    public List<ExamsDTO> getExams() {
        List<Exams> examsList = examsRepo.findAll();
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    // Retrieves a exam entry by ID
    public ExamsDTO getExamEntryById(Integer id) {
        Exams exams = examsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "exam with id " + id + " does not exist"));
        ExamsDTO examsDTO = examsMapper.entityToDTO(exams);
        return examsDTO;
    }

    public List<ExamsDTO> getByStudentForEvent(String token, Integer eventID) {
        String userEmail = jwtService.extractUsername(token);
        List<Exams> examsList = examsRepo.findByStudentForEvent(userEmail, eventID);
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    public List<ExamsDTO> getByProfesorForEvent(String token, Integer eventID) {
        String userEmail = jwtService.extractUsername(token);
        List<Exams> examsList = examsRepo.findExamsForEventByProfesor(userEmail, eventID);
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    public List<ExamsDTO> getByEvent(Integer eventID) {
        List<Exams> examsList = examsRepo.findByEvent(eventID);
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    public List<ExamsDTO> getBySubjectForEvent(Integer subjectID, Integer eventID) {
        List<Exams> examsList = examsRepo.findBySubjectForEvent(subjectID, eventID);
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    public void addNewExam(String token, Integer profesorID, Integer subjectID) {
        Exams exam = new Exams();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = java.util.Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Events ongoingEvent = eventsRepo.checkIfEventOngoing(currentDate);
        if(ongoingEvent == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No exam registration period active at the moment");
        }
        String userEmail = jwtService.extractUsername(token);

        Student student = studentRepo.findByEmail(userEmail);

        Employee profesor = employeeRepo.findById(profesorID).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor not found"));

        Subjects subject =  subjectsRepo.findById(subjectID).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        Exams checksIfAlreadyRegistered = examsRepo.checkIfExamAlreadyRegisteredForEventOngoing(subjectID, userEmail, currentDate);

        if(checksIfAlreadyRegistered != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already registered for that exam in this exam period");
        }
        exam.setSubject(subject);
        exam.setCreatedAt(currentDate);
        exam.setStudent(student);
        exam.setProfesor(profesor);
        exam.setEvent(ongoingEvent);
        examsRepo.save(exam);

        List<Exams> examsList = examsRepo.findHowManyTimesDidStudentRegisterAnExam(subjectID, userEmail);

        if(ongoingEvent.getType() == Type_of_event.EXAM_REGISTRATION_LATE){
            Finances finances = new Finances();
            Finances financesRegular = new Finances();

            finances.setStudent(student);
            finances.setAmount(800);
            finances.setCreatedAt(currentDate);
            finances.setIdExam(exam);
            finances.setCurrency(Currency.RSD);
            finances.setNote("Late exam registration fee");
            finances.setStatus(Finances_status.ACTIVE);

            financesRegular.setStudent(student);
            if(examsList.size() < 3){
                financesRegular.setAmount(300);
            }
            else financesRegular.setAmount(1500);
            financesRegular.setCreatedAt(currentDate);
            financesRegular.setIdExam(exam);
            financesRegular.setCurrency(Currency.RSD);
            financesRegular.setNote("Regular exam registration fee");
            financesRegular.setStatus(Finances_status.ACTIVE);

            financesRepo.save(financesRegular);
            financesRepo.save(finances);
        }
        else if(ongoingEvent.getType() == Type_of_event.EXAM_REGISTRATION){
            Finances financesRegular = new Finances();

            financesRegular.setStudent(student);
            if(examsList.size() < 3){
                financesRegular.setAmount(300);
            }
            else financesRegular.setAmount(1500);
            financesRegular.setCreatedAt(currentDate);
            financesRegular.setIdExam(exam);
            financesRegular.setCurrency(Currency.RSD);
            financesRegular.setNote("Regular exam registration fee");
            financesRegular.setStatus(Finances_status.ACTIVE);

            financesRepo.save(financesRegular);
        }
    }
    public void removeExam(String token, Integer eventID, Integer subjectID){
        String userEmail = jwtService.extractUsername(token);

        Exams exam = examsRepo.findIfEntryExists(subjectID, eventID, userEmail);

        Events examPeriod = eventsRepo.findById(eventID).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        Date examPeriodStart = examPeriod.getStartDate();
        LocalDate localExamPeriodStart = examPeriodStart.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = java.util.Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime localExamPeriodStartDateTime = localExamPeriodStart.atStartOfDay();
        boolean isAfterExamPeriodStart = currentDateTime.isAfter(localExamPeriodStartDateTime);

        if (isAfterExamPeriodStart) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Exam period has already started, can't cancel now");
        }

        List<Finances> finances = financesRepo.financesForSpecificExamEntry(userEmail, exam.getId());

        for (Finances financesDelete : finances) {
            financesRepo.delete(financesDelete);
        }
        examsRepo.delete(exam);
    }
}
