package com.asss.management.service.implementation;

import com.asss.management.dao.*;
import com.asss.management.entity.*;
import com.asss.management.entity.Enums.Type_of_event;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.AssignedProfesorsDTO;
import com.asss.management.service.dto.PassedExamsDTO;
import com.asss.management.service.mapper.PassedExamsMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Key;
import java.util.List;
import java.util.Objects;

@Service
@Data
public class PassedExamsService {

    private final PassedExamsRepo passedExamsRepo;
    private final PassedExamsMapper passedExamsMapper;
    private final EmployeeRepo employeeRepo;
    private final EventsRepo eventsRepo;
    private final StudentRepo studentRepo;
    private final SubjectsRepo subjectsRepo;
    private final ExamsRepo examsRepo;
    private final JwtService jwtService;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // get all passed exams
    public List<PassedExamsDTO> getPassedExams(){
        List<PassedExams> passedExamsList = passedExamsRepo.findAll();
        List<PassedExamsDTO> passedExamsDTOList = passedExamsMapper.entitiesToDTOs(passedExamsList);
        return passedExamsDTOList;
    }

    // Retrieves a passed exam by id
    public PassedExamsDTO getPassedExamById(Integer id) {
        PassedExams passedExams = passedExamsRepo.findById(id).orElse(null);
        PassedExamsDTO passedExamsDTO = passedExamsMapper.entityToDTO(passedExams);
        return passedExamsDTO;
    }

    public List<PassedExamsDTO> getPassedExamsByProfesor(String token) {
        String userEmail = jwtService.extractUsername(token);
        List<PassedExams> passedExamsList = passedExamsRepo.passedExamsByProfesor(userEmail);
        List<PassedExamsDTO> passedExamsDTOList = passedExamsMapper.entitiesToDTOs(passedExamsList);
        return passedExamsDTOList;
    }

    public List<PassedExamsDTO> getPassedExamsByStudent(String token) {
        String userEmail = jwtService.extractUsername(token);
        List<PassedExams> passedExamsList = passedExamsRepo.passedExamsByStudent(userEmail);
        List<PassedExamsDTO> passedExamsDTOList = passedExamsMapper.entitiesToDTOs(passedExamsList);
        return passedExamsDTOList;
    }

    @Transactional
    public void updatePassedExam(Integer id,
                              Integer grade){
        PassedExams passedExams = passedExamsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified passed exam has not been found."));

        if (grade != null && !Objects.equals(passedExams.getGrade(), grade)) {
            passedExams.setGrade(grade);
        }
    }

    public void addNewPassedExam(PassedExams passedExams, String index, Integer profesorID, Integer subjectID, Integer eventID) {
        Employee employee = employeeRepo.findById(profesorID).orElse(null);
        Events events = eventsRepo.findById(eventID).orElse(null);
        Student student = studentRepo.findByIndex(index);
        Subjects subject = subjectsRepo.findById(subjectID).orElse(null);

        Exams reportedExam = examsRepo.findIfStudentRegistratedTheExam(subjectID, eventID, index);
        if(reportedExam == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student never registered for that exam for that event");
        }

        PassedExams passedExamCheck = passedExamsRepo.checkIfExamAlreadyPassed(subjectID, index);

        if(events.getType() != Type_of_event.EXAM_REGISTRATION && events.getType() != Type_of_event.EXAM_REGISTRATION_LATE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are trying to add passed exam for a non Exam period event");
        }
        if(passedExamCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already passed that exam");
        }

        if(subject.getCourse() != student.getCourseOfStudies()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong input");
        }
        if(passedExams.getGrade() < 6 || passedExams.getGrade() > 10){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Grade scope exceeded");
        }
        passedExams.setEvent(events);
        passedExams.setProfesor(employee);
        passedExams.setStudent(student);
        passedExams.setSubject(subject);

        passedExamsRepo.save(passedExams);
    }

    public void addNewPassedExamAsProfesor(PassedExams passedExams, String index, Integer subjectID, Integer eventID, String token) {
        String userEmail = jwtService.extractUsername(token);

        Employee employee = employeeRepo.findByEmail(userEmail);
        Events events = eventsRepo.findById(eventID).orElse(null);
        Student student = studentRepo.findByIndex(index);
        Subjects subject = subjectsRepo.findById(subjectID).orElse(null);

        Exams reportedExam = examsRepo.findIfStudentRegistratedTheExam(subjectID, eventID, index);
        if(reportedExam == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student never registered for that exam for that event");
        }

        PassedExams passedExamCheck = passedExamsRepo.checkIfExamAlreadyPassed(subjectID, index);

        if(events.getType() != Type_of_event.EXAM_REGISTRATION && events.getType() != Type_of_event.EXAM_REGISTRATION_LATE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are trying to add passed exam for a non Exam period event");
        }
        if(passedExamCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already passed that exam");
        }

        if(subject.getCourse() != student.getCourseOfStudies()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong input");
        }
        if(passedExams.getGrade() < 6 || passedExams.getGrade() > 10){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Grade scope exceeded");
        }
        passedExams.setEvent(events);
        passedExams.setProfesor(employee);
        passedExams.setStudent(student);
        passedExams.setSubject(subject);

        passedExamsRepo.save(passedExams);
    }
}
