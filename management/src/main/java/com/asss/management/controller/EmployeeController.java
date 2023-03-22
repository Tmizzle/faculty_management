package com.asss.management.controller;

import com.asss.management.entity.Employee;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.EmployeeDTO;
import com.asss.management.service.implementation.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(path = "/api/employee")
@Data
@Tag(name = "Employee API", description = "API for managing employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtService jwtService;

    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class))))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IllegalStateException.class)))
    @GetMapping
    public List<EmployeeDTO> getEmployees(){
        return employeeService.getEmployee();
    }

    @GetMapping(path = "/getEmployeeByEmail/")
    public EmployeeDTO getEmployeeByEmail(@RequestParam String token, HttpServletResponse response){
        return employeeService.getUserInfoFromToken(token);
    }

    @PutMapping(path = "{token}")
    public ResponseEntity updateEmployee(
            @PathVariable("token") String token,
            @Parameter(description = "Employee first name") @RequestParam(required = false) String firstName,
            @Parameter(description = "Employee last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Employee middle name") @RequestParam(required = false) String middleName,
            @Parameter(description = "Employee email") @RequestParam(required = false) String email
    ) {
        employeeService.updateEmployee(token, firstName, lastName, middleName, email);

        return ResponseEntity.ok("Employee updated successfully");
    }

    @PostMapping
    public ResponseEntity addNewEmployee(@RequestBody Employee employee){
        employeeService.addNewEmployee(employee);

        return ResponseEntity.ok("Added a new employee successfully");
    }
}
