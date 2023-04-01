package com.asss.management.service.implementation;

import com.asss.management.dao.*;
import com.asss.management.entity.*;
import com.asss.management.entity.Enums.Currency;
import com.asss.management.entity.Enums.Exam_status;
import com.asss.management.entity.Enums.Finances_status;
import com.asss.management.entity.Enums.Year_of_studies;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.StudentHistoryDTO;
import com.asss.management.service.mapper.StudentHistoryMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Data
public class StudentHistoryService {

    private final StudentHistoryRepo studentHistoryRepo;
    private final StudentHistoryMapper studentHistoryMapper;
    private final EmployeeRepo employeeRepo;
    private final StudentRepo studentRepo;
    private final JwtService jwtService;
    private final ExamStatusInfoRepo examStatusInfoRepo;
    private final FinancesRepo financesRepo;
    private final SubjectsRepo subjectsRepo;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Retrieves all student history
    public List<StudentHistoryDTO> getStudentHistory() {
        List<StudentHistory> studentHistoryList = studentHistoryRepo.findAll();
        List<StudentHistoryDTO> studentHistoryDTOList = studentHistoryMapper.entitiesToDTOs(studentHistoryList);
        return studentHistoryDTOList;
    }

    public StudentHistoryDTO getStudentHistoryById(Integer id) {
        StudentHistory studentHistory = studentHistoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified student history entry has not been found."));
        StudentHistoryDTO studentHistoryDTO = studentHistoryMapper.entityToDTO(studentHistory);
        return studentHistoryDTO;
    }

    public List<StudentHistoryDTO> getStudentHistoryByIndex(String index) {
        List<StudentHistory> studentHistoryList = studentHistoryRepo.studentHistoryByIndex(index);
        List<StudentHistoryDTO> studentHistoryDTOList = studentHistoryMapper.entitiesToDTOs(studentHistoryList);
        return studentHistoryDTOList;
    }

    public List<StudentHistoryDTO> getStudentHistoryByLoggedUser(String token) {
        String userEmail = jwtService.extractUsername(token);
        List<StudentHistory> studentHistoryList = studentHistoryRepo.studentHistoryByLoggedUser(userEmail);
        List<StudentHistoryDTO> studentHistoryDTOList = studentHistoryMapper.entitiesToDTOs(studentHistoryList);
        return studentHistoryDTOList;
    }

    public void addNewStudentHistoryEntry(StudentHistory studentHistory, String token, String index) {
        String userEmail = jwtService.extractUsername(token);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = java.util.Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Employee createdBy = employeeRepo.findByEmail(userEmail);
        Student student = studentRepo.findByIndex(index);

        if(student == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Specified student has not been found");
        }

        if(studentHistory.getYearOfStudies() == student.getYearOfStudies()){
            student.setRenewed(true);
        } else {
            student.setRenewed(false);
            List<Subjects> subjectsList = subjectsRepo.findByYearForCourse(studentHistory.getYearOfStudies(), student.getCourseOfStudies());

            for (Subjects subjects : subjectsList) {
                ExamStatusInfo examStatusInfo = new ExamStatusInfo();
                examStatusInfo.setStudent(student);
                examStatusInfo.setStatus(Exam_status.UNPASSED);
                examStatusInfo.setGrade(5);
                examStatusInfo.setExamPoints(0);
                examStatusInfo.setProfesor(null);
                examStatusInfo.setColloquiumThree(0);
                examStatusInfo.setColloquiumOne(0);
                examStatusInfo.setColloquiumTwo(0);
                examStatusInfo.setSubject(subjects);
                examStatusInfo.setEvent(null);

                examStatusInfoRepo.save(examStatusInfo);
            }
        }

        if(studentHistory.getYearOfStudies() == Year_of_studies.FIRST_YEAR){
            if(student.getYearOfStudies() != Year_of_studies.FIRST_YEAR){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student has already been registered for that year of studies");
            }
        }

        if(studentHistory.getYearOfStudies() == Year_of_studies.SECOND_YEAR){
            if(student.getYearOfStudies() == Year_of_studies.THIRD_YEAR){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student has already been registered for that year of studies");
            }
        }
        if(studentHistory.getYearOfStudies() == Year_of_studies.THIRD_YEAR){
            if(student.getYearOfStudies() == Year_of_studies.FIRST_YEAR){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Students needs to finish all years previous to this one");
            }
        }


        List<ExamStatusInfo> unpassedExams = examStatusInfoRepo.unpassedExamsByStudent(student.getEmail());

        int totalEspb = 0;
        for (ExamStatusInfo exam : unpassedExams) {
            totalEspb += exam.getSubject().getEspb();
        }
        System.out.println(totalEspb);
        if(totalEspb>0){
            Finances finances = new Finances();

            int amount = totalEspb * 1167;

            finances.setStudent(student);
            finances.setAmount(amount);
            finances.setCreatedAt(currentDate);
            finances.setIdExam(null);
            finances.setCurrency(Currency.RSD);
            finances.setNote("ESPB transfer");
            finances.setStatus(Finances_status.ACTIVE);

            financesRepo.save(finances);
        }
        if(!studentHistory.getBudget()){
            Finances finances = new Finances();

            finances.setStudent(student);
            finances.setAmount(70000);
            finances.setCreatedAt(currentDate);
            finances.setIdExam(null);
            finances.setCurrency(Currency.RSD);
            finances.setNote("New year fee for non budget student");
            finances.setStatus(Finances_status.ACTIVE);

            financesRepo.save(finances);
        }
        Finances finances = new Finances();

        finances.setStudent(student);
        finances.setAmount(2500);
        finances.setCreatedAt(currentDate);
        finances.setIdExam(null);
        finances.setCurrency(Currency.RSD);
        finances.setNote("New year entry fee");
        finances.setStatus(Finances_status.ACTIVE);
        financesRepo.save(finances);

        student.setBudget(studentHistory.getBudget());
        student.setUpdatedAt(currentDate);
        student.setUpdatedBy(createdBy);
        student.setYearOfStudies(studentHistory.getYearOfStudies());

        studentHistory.setStudent(student);
        studentHistory.setCreatedAt(currentDate);
        studentHistory.setCreatedBy(createdBy);
        studentHistoryRepo.save(studentHistory);
    }
}
