package com.asss.management.service.implementation;

import com.asss.management.dao.EmployeeRepo;
import com.asss.management.entity.Employee;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Data
public class LoginService {

    private final EmployeeRepo employeeRepo;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private String generateToken(Employee employee, Key key) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", employee.getId());
        claims.put("email", employee.getEmail());
        claims.put("firstName", employee.getFirstName());
        claims.put("lastName", employee.getLastName());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000); // Token expires in 24 hours

        return Jwts.builder()
                .setSubject(employee.getEmail())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String LoginEmployee(String email, String password){
        Employee employee = employeeRepo.loginEmployeeParams(email, password);
        if(employee == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong params");
        }
        String token = generateToken(employee, key);
        return token;
    }
}
