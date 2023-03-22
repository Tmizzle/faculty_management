package com.asss.management.service.implementation;

import com.asss.management.dao.EmployeeRepo;
import com.asss.management.dao.StudentHistoryRepo;
import com.asss.management.dao.StudentRepo;
import com.asss.management.entity.Employee;
import com.asss.management.entity.Enums.Year_of_studies;
import com.asss.management.entity.Student;
import com.asss.management.entity.StudentHistory;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.StudentHistoryDTO;
import com.asss.management.service.mapper.StudentHistoryMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        }
        else student.setRenewed(false);
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
