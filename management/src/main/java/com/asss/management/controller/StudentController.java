package com.asss.management.controller;

import com.asss.management.service.dto.StudentDTO;
import com.asss.management.service.implementation.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/students")
@Data
@Tag(name = "Students API", description = "API for managing students")
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
}
