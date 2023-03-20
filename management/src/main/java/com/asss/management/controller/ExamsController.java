package com.asss.management.controller;

import com.asss.management.service.dto.ExamsDTO;
import com.asss.management.service.dto.StudentHistoryDTO;
import com.asss.management.service.implementation.ExamsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/exams")
@Data
@Tag(name = "Exams API", description = "API for managing exams")
@CrossOrigin(origins = "*")
public class ExamsController {

    private final ExamsService examsService;

    @GetMapping
    public List<ExamsDTO> getExams(){
        return examsService.getExams();
    }
}
