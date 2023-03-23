package com.asss.management.securityConfig;

import com.asss.management.dao.EmployeeRepo;
import com.asss.management.dao.StudentRepo;
import com.asss.management.entity.Employee;
import com.asss.management.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ApplicationConfig {

    private final EmployeeRepo employeeRepo;
    private final StudentRepo studentRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<Employee> employee = employeeRepo.findByEmailSecurity(username);
            if (employee.isPresent()) {
                return employee.get();
            } else {
                Optional<Student> student = studentRepo.findByEmailSecurity(username);
                if (student.isPresent()) {
                    return student.get();
                } else {
                    throw new UsernameNotFoundException("User not found");
                }
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
