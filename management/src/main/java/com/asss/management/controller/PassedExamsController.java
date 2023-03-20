package com.asss.management.controller;

import com.asss.management.entity.PassedExams;
import com.asss.management.entity.Student;
import com.asss.management.service.dto.AssignedProfesorsDTO;
import com.asss.management.service.dto.PassedExamsDTO;
import com.asss.management.service.implementation.PassedExamsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/passed_exams")
@Data
@Tag(name = "Passed exams API", description = "API for managing passed exams")
@CrossOrigin(origins = "*")
public class PassedExamsController {

    private final PassedExamsService passedExamsService;

    @GetMapping
    public List<PassedExamsDTO> getPassedExams(){
        return passedExamsService.getPassedExams();
    }

    @GetMapping(path = "{id}")
    public PassedExamsDTO getPassedExamById(@PathVariable("id") Integer id){
        return passedExamsService.getPassedExamById(id);
    }

    @GetMapping(path = "/getPassedExamsByProfesor/")
    public List<PassedExamsDTO> getPassedExamsByProfesor(@RequestParam String token){
        return passedExamsService.getPassedExamsByProfesor(token);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity updatePassedExam(
            @PathVariable("id") Integer id,
            @Parameter(description = "Exam grade") @RequestParam Integer grade
    ) {
        passedExamsService.updatePassedExam(id, grade);

        return ResponseEntity.ok("Passed exam updated successfully");
    }

    @PostMapping
    public ResponseEntity addNewPassedExam(@RequestBody PassedExams passedExams,
                                        @RequestParam Integer eventID,
                                        @RequestParam Integer studentID,
                                        @RequestParam Integer subjectID,
                                        @RequestParam Integer profesorID){
        passedExamsService.addNewPassedExam(passedExams, studentID, profesorID, subjectID, eventID);

        return ResponseEntity.ok("Added a new passed exam successfully");
    }

    @PostMapping(path = "/addNewPassedExamAsProfesor/")
    public ResponseEntity addNewPassedExamAsProfesor(@RequestBody PassedExams passedExams,
                                        @RequestParam Integer eventID,
                                        @RequestParam Integer studentID,
                                        @RequestParam Integer subjectID,
                                        @RequestParam String token){
        passedExamsService.addNewPassedExamAsProfesor(passedExams, studentID, subjectID, eventID, token);

        return ResponseEntity.ok("Added a new passed exam successfully");
    }
}
