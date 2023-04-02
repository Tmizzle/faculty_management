package com.asss.management.controller;

import com.asss.management.service.dto.ExamsDTO;
import com.asss.management.service.implementation.ExamsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "{id}")
    public ExamsDTO getExamsEntryById(@PathVariable("id") Integer id){
        return examsService.getExamEntryById(id);
    }

    @GetMapping(path = "/getExamsByStudentForEvent/")
    public List<ExamsDTO> getExamsByStudentForEvent(@RequestParam String token,
                                                    @RequestParam Integer eventID){
        return examsService.getByStudentForEvent(token, eventID);
    }

    @GetMapping(path = "/getExamsByProfesorForEvent/")
    public List<ExamsDTO> getExamsByProfesorForEvent(@RequestParam String token,
                                                    @RequestParam Integer eventID){
        return examsService.getByProfesorForEvent(token, eventID);
    }

    @GetMapping(path = "/getExamsByEvent/")
    public List<ExamsDTO> getExamsByEvent(@RequestParam Integer eventID){
        return examsService.getByEvent(eventID);
    }

    @GetMapping(path = "/getExamsBySubjectForEvent/")
    public List<ExamsDTO> getExamsBySubjectForEvent(@RequestParam Integer subjectID, @RequestParam Integer eventID){
        return examsService.getBySubjectForEvent(subjectID, eventID);
    }

    @PostMapping
    public ResponseEntity addNewExam(@RequestParam String token,
                                     @RequestParam Integer profesorID,
                                     @RequestParam Integer subjectID){
        examsService.addNewExam(token, profesorID, subjectID);

        return ResponseEntity.ok("Added a new exam registration successfully");
    }

    @DeleteMapping(path = "/removeExam/")
    public ResponseEntity removeExam(@RequestParam String token,
                                     @RequestParam Integer eventID,
                                     @RequestParam Integer subjectID){
        examsService.removeExam(token, eventID, subjectID);

        return ResponseEntity.ok("Removed an exam successfully");
    }
}
