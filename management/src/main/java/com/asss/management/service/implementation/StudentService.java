package com.asss.management.service.implementation;

import com.asss.management.dao.*;
import com.asss.management.entity.*;
import com.asss.management.entity.Enums.*;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.EmployeeDTO;
import com.asss.management.service.dto.StudentDTO;
import com.asss.management.service.dto.SubjectsDTO;
import com.asss.management.service.mapper.StudentMapper;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Data
public class StudentService {

    private final StudentRepo studentRepo;
    private final StudentMapper studentMapper;
    private final EmployeeRepo employeeRepo;
    private final JwtService jwtService;
    private final SubjectsRepo subjectsRepo;
    private final ExamStatusInfoRepo examStatusInfoRepo;
    private final StudentHistoryRepo studentHistoryRepo;
    private final FinancesRepo financesRepo;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    // Retrieves all students
    public List<StudentDTO> getStudents() {
        List<Student> studentList = studentRepo.findAll();
        return studentMapper.entitiesToDTOs(studentList);
    }

    // Retrieves a student by index
    public StudentDTO getStudentByIndex(String index) {
        Student student = studentRepo.findByIndex(index);
        if(student == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with that index doesn't exist");
        }
        return studentMapper.entityToDTO(student);
    }

    public StudentDTO getUserInfoFromToken(String token) {
        String userEmail = jwtService.extractUsername(token);
        Student student = studentRepo.findByEmail(userEmail);
        return studentMapper.entityToDTO(student);
    }

    // add new student
    public void addNewStudent(Student student, String token) {

        String userEmail = jwtService.extractUsername(token);

        Employee employee = employeeRepo.findByEmail(userEmail);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = java.util.Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Student emailCheck = studentRepo.findByEmail(student.getEmail());
        // Internal code unique check
        if (emailCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        Student jmbgCheck = studentRepo.findByJMBG(student.getJmbg());
        if(jmbgCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "JMBG already in use");
        }
        student.setCreatedAt(currentDate);
        student.setCreatedBy(employee);
        student.setYearOfStudies(Year_of_studies.FIRST_YEAR);
        student.setRenewed(false);
        student.setTypeOfStudies(Type_of_studies.REGULAR);
        student.setStatus(Student_status.ACTIVE);
        student.setOldPassword(null);
        student.setUpdatedAt(null);
        student.setUpdatedBy(null);
        studentRepo.save(student);

        StudentHistory studentHistory = new StudentHistory();
        studentHistory.setStudent(student);
        studentHistory.setCreatedAt(currentDate);
        studentHistory.setCreatedBy(employee);
        studentHistory.setYearOfStudies(Year_of_studies.FIRST_YEAR);
        studentHistory.setBudget(student.getBudget());

        studentHistoryRepo.save(studentHistory);

        if(!student.getBudget()){
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

        List<Subjects> subjectsList = subjectsRepo.findByYearForCourse(Year_of_studies.FIRST_YEAR, student.getCourseOfStudies());

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

    // Updated student
    @Transactional
    public void updateStudent(Integer id,
                               String token,
                               String firstName,
                               String lastName,
                               String middleName,
                               String email){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = java.util.Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        String userEmail = jwtService.extractUsername(token);

        Employee employee = employeeRepo.findByEmail(userEmail);

        Student student = studentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified student has not been found."));

        if (firstName != null && !Objects.equals(student.getFirstName(), firstName)) {
            student.setFirstName(firstName);
            student.setUpdatedAt(currentDate);
            student.setUpdatedBy(employee);
        }
        if (lastName != null && !Objects.equals(student.getLastName(), lastName)) {
            student.setLastName(lastName);
            student.setUpdatedAt(currentDate);
            student.setUpdatedBy(employee);
        }
        if (middleName != null && !Objects.equals(student.getMiddleName(), middleName)) {
            student.setMiddleName(middleName);
            student.setUpdatedAt(currentDate);
            student.setUpdatedBy(employee);
        }
        if (email != null && !Objects.equals(student.getEmail(), email)) {
            Student emailCheck = studentRepo.findByEmail(email);
            if(emailCheck != null){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }
            student.setEmail(email);
            student.setUpdatedAt(currentDate);
            student.setUpdatedBy(employee);
        }

    }
}
