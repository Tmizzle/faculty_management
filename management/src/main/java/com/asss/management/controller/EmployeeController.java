package com.asss.management.controller;

import com.asss.management.entity.Employee;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/employee")
@Data
@Tag(name = "Employee API", description = "API for managing employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;

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

    @GetMapping(path = "/getEmployeeById/")
    public EmployeeDTO getEmployeeById(@RequestParam String token){
        return employeeService.getUserInfoFromToken(token);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity updateEmployee(
            @PathVariable("id") Integer id,
            @Parameter(description = "Session token") @RequestParam String token,
            @Parameter(description = "Employee first name") @RequestParam(required = false) String firstName,
            @Parameter(description = "Employee last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Employee middle name") @RequestParam(required = false) String middleName,
            @Parameter(description = "Employee email") @RequestParam(required = false) String email
    ) {
        employeeService.updateEmployee(id, token, firstName, lastName, middleName, email);

        return ResponseEntity.ok("Employee updated successfully");
    }

    @PostMapping
    public ResponseEntity addNewEmployee(@RequestBody Employee employee){
        employeeService.addNewEmployee(employee);

        return ResponseEntity.ok("Added a new employee successfully");
    }
}
