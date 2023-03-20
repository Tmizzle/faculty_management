package com.asss.management.controller;

import com.asss.management.entity.StudentHistory;
import com.asss.management.service.dto.StudentHistoryDTO;
import com.asss.management.service.implementation.StudentHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/student_history")
@Data
@Tag(name = "Student history API", description = "API for managing student history")
@CrossOrigin(origins = "*")
public class StudentHistoryController {

    private final StudentHistoryService studentHistoryService;

    @GetMapping
    public List<StudentHistoryDTO> getStudentHistory(){
        return studentHistoryService.getStudentHistory();
    }

    @GetMapping(path = "{id}")
    public StudentHistoryDTO getById(@PathVariable("id") Integer id){
        return studentHistoryService.getStudentHistoryById(id);
    }

    @GetMapping(path = "/getByIndex/")
    public List<StudentHistoryDTO> getByIndex(@RequestParam String index){
        return studentHistoryService.getStudentHistoryByIndex(index);
    }

    @PostMapping
    public ResponseEntity addNewStudentHistory(@RequestBody StudentHistory studentHistory, @RequestParam String token, @RequestParam String index){
        studentHistoryService.addNewStudentHistoryEntry(studentHistory, token, index);

        return ResponseEntity.ok("Added a new student history successfully");
    }
}
