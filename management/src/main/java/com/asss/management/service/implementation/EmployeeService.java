package com.asss.management.service.implementation;

import com.asss.management.dao.EmployeeRepo;
import com.asss.management.entity.Employee;
import com.asss.management.entity.Enums.Employee_category;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.EmployeeDTO;
import com.asss.management.service.mapper.EmployeeMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Retrieves all employees
    public List<EmployeeDTO> getEmployee() {
        List<Employee> employeeList = employeeRepo.findAll();
        return employeeMapper.entitiesToDTOs(employeeList);
    }

    public EmployeeDTO getUserInfoFromToken(String token) {
        String userEmail = jwtService.extractUsername(token);
        Employee employee = employeeRepo.findByEmail(userEmail);
        return employeeMapper.entityToDTO(employee);
    }

    public List<EmployeeDTO> getProfessors() {
        List<Employee> employeeList = employeeRepo.findProfessors(Employee_category.PROFESOR);
        return employeeMapper.entitiesToDTOs(employeeList);
    }

    @Transactional
    public void updateEmployee(String token,
                                   String firstName,
                                   String lastName,
                                   String middleName,
                                    String email){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = java.util.Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        String userEmail = jwtService.extractUsername(token);

        Employee employee = employeeRepo.findByEmail(userEmail);

        if (employee == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (firstName != null && !Objects.equals(employee.getFirstName(), firstName)) {
            employee.setFirstName(firstName);
            employee.setUpdatedAt(currentDate);
        }
        if (lastName != null && !Objects.equals(employee.getLastName(), lastName)) {
            employee.setLastName(lastName);
            employee.setUpdatedAt(currentDate);
        }
        if (middleName != null && !Objects.equals(employee.getMiddleName(), middleName)) {
            employee.setMiddleName(middleName);
            employee.setUpdatedAt(currentDate);
        }
        if (email != null && !Objects.equals(employee.getEmail(), email)) {
            Employee emailCheck = employeeRepo.findByEmail(email);
            if(emailCheck != null){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }
            employee.setEmail(email);
            employee.setUpdatedAt(currentDate);
        }

    }
    // add new employee
    public void addNewEmployee(Employee employee) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = java.util.Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Employee emailCheck = employeeRepo.findByEmail(employee.getEmail());
        // Internal code unique check
        if (emailCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        Employee jmbgCheck = employeeRepo.findByJMBG(employee.getJmbg());
        if(jmbgCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "JMBG already in use");
        }
        employee.setCreatedAt(currentDate);
        employeeRepo.save(employee);
    }
}
