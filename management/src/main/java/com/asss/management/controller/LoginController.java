package com.asss.management.controller;

import com.asss.management.service.implementation.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/public/login")
@Data
@Tag(name = "Login API", description = "API for managing logins")
@CrossOrigin(origins = "*")
public class LoginController {

    private final LoginService loginService;

    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(loginService.authenticate(request));
    }
}
