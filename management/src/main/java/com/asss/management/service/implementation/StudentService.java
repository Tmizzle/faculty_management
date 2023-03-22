package com.asss.management.service.implementation;

import com.asss.management.dao.EmployeeRepo;
import com.asss.management.dao.StudentRepo;
import com.asss.management.entity.Employee;
import com.asss.management.entity.Student;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.EmployeeDTO;
import com.asss.management.service.dto.StudentDTO;
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

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    // Retrieves all students
    public List<StudentDTO> getStudents() {
        List<Student> studentList = studentRepo.findAll();
        List<StudentDTO> studentDTOList = studentMapper.entitiesToDTOs(studentList);
        return studentDTOList;
    }

    // Retrieves a student by index
    public StudentDTO getStudentByIndex(String index) {
        Student student = studentRepo.findByIndex(index);
        if(student == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with that index doesn't exist");
        }
        StudentDTO studentDTO = studentMapper.entityToDTO(student);
        return studentDTO;
    }

    public StudentDTO getUserInfoFromToken(String token) {
        String userEmail = jwtService.extractUsername(token);
        Student student = studentRepo.findByEmail(userEmail);
        StudentDTO studentDTO = studentMapper.entityToDTO(student);
        return studentDTO;
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
        studentRepo.save(student);
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
