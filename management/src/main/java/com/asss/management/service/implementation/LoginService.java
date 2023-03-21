package com.asss.management.service.implementation;

import com.asss.management.controller.AuthenticationRequest;
import com.asss.management.controller.AuthenticationResponse;
import com.asss.management.dao.EmployeeRepo;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.mapper.EmployeeMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@Data
@RequiredArgsConstructor
public class LoginService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = employeeRepo.findByEmailSecurity(request.getEmail()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}
