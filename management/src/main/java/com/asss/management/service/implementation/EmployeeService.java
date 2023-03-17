package com.asss.management.service.implementation;

import com.asss.management.dao.EmployeeRepo;
import com.asss.management.entity.Employee;
import com.asss.management.service.dto.EmployeeDTO;
import com.asss.management.service.mapper.EmployeeMapper;
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
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Retrieves all employees
    public List<EmployeeDTO> getEmployee() {
        List<Employee> employeeList = employeeRepo.findAll();
        List<EmployeeDTO> employeeDTOList = employeeMapper.entitiesToDTOs(employeeList);
        return employeeDTOList;
    }

    public EmployeeDTO getUserInfoFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        Integer userId = claims.get("id", Integer.class);
        Employee employee = employeeRepo.findById(userId).orElse(null);
        EmployeeDTO employeeDTO = employeeMapper.entityToDTO(employee);
        return employeeDTO;
    }

    @Transactional
    public void updateEmployee(Integer id,
                                   String token,
                                   String firstName,
                                   String lastName,
                                   String middleName,
                                    String email){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = java.util.Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        Integer userId = claims.get("id", Integer.class);

        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified employee has not been found."));

        if (firstName != null && !Objects.equals(employee.getFirstName(), firstName)) {
            employee.setFirstName(firstName);
            employee.setUpdatedAt(currentDate);
            employee.setUpdatedBy(userId);
        }
        if (lastName != null && !Objects.equals(employee.getLastName(), lastName)) {
            employee.setLastName(lastName);
            employee.setUpdatedAt(currentDate);
            employee.setUpdatedBy(userId);
        }
        if (middleName != null && !Objects.equals(employee.getMiddleName(), middleName)) {
            employee.setMiddleName(middleName);
            employee.setUpdatedAt(currentDate);
            employee.setUpdatedBy(userId);
        }
        if (email != null && !Objects.equals(employee.getEmail(), email)) {
            Employee emailCheck = employeeRepo.findByEmail(email);
            if(emailCheck != null){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }
            employee.setEmail(email);
            employee.setUpdatedAt(currentDate);
            employee.setUpdatedBy(userId);
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
