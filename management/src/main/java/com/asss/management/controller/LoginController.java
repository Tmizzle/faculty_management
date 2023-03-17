package com.asss.management.controller;

import com.asss.management.service.implementation.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/login")
@Data
@Tag(name = "Login API", description = "API for managing logins")
@CrossOrigin(origins = "*")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<Map<String, String>> loginEmployee(@RequestParam String email, @RequestParam String password) {
        String token = loginService.LoginEmployee(email, password);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok(responseBody);
    }
}
