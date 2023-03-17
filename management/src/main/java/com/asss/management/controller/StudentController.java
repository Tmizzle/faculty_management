package com.asss.management.controller;

import com.asss.management.entity.Employee;
import com.asss.management.entity.Student;
import com.asss.management.service.dto.EmployeeDTO;
import com.asss.management.service.dto.StudentDTO;
import com.asss.management.service.implementation.StudentService;
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
@RequestMapping(path = "/api/students")
@Data
@Tag(name = "Students API", description = "API for managing students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class))))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IllegalStateException.class)))
    @GetMapping
    public List<StudentDTO> getStudents(){
        return studentService.getStudents();
    }

    @GetMapping(path = "/getStudentByIndex")
    public StudentDTO getStudentByIndex(@RequestParam String index){
        return studentService.getStudentByIndex(index);
    }

    @PostMapping
    public ResponseEntity addNewStudent(@RequestBody Student student, @RequestParam String token){
        studentService.addNewStudent(student, token);

        return ResponseEntity.ok("Added a new student successfully");
    }

    @PutMapping(path = "{id}")
    public ResponseEntity updateEmployee(
            @PathVariable("id") Integer id,
            @Parameter(description = "Session token") @RequestParam String token,
            @Parameter(description = "Student first name") @RequestParam(required = false) String firstName,
            @Parameter(description = "Student last name") @RequestParam(required = false) String lastName,
            @Parameter(description = "Student middle name") @RequestParam(required = false) String middleName,
            @Parameter(description = "Student email") @RequestParam(required = false) String email
    ) {
        studentService.updateStudent(id, token, firstName, lastName, middleName, email);

        return ResponseEntity.ok("Student updated successfully");
    }
}
