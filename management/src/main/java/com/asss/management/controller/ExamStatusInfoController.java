package com.asss.management.controller;

import com.asss.management.entity.ExamStatusInfo;
import com.asss.management.service.dto.ExamStatusInfoDTO;
import com.asss.management.service.implementation.ExamStatusInfoService;
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
public class ExamStatusInfoController {

    private final ExamStatusInfoService examStatusInfoService;

    @GetMapping
    public List<ExamStatusInfoDTO> getPassedExams(){
        return examStatusInfoService.getPassedExams();
    }

    @GetMapping(path = "{id}")
    public ExamStatusInfoDTO getPassedExamById(@PathVariable("id") Integer id){
        return examStatusInfoService.getPassedExamById(id); 
    }

    @GetMapping(path = "/getPassedExamsByProfesor/")
    public List<ExamStatusInfoDTO> getPassedExamsByProfesor(@RequestParam String token){
        return examStatusInfoService.getPassedExamsByProfesor(token);
    }

    @GetMapping(path = "/getPassedExamsByStudent/")
    public List<ExamStatusInfoDTO> getPassedExamsByStudent(@RequestParam String token){
        return examStatusInfoService.getPassedExamsByStudent(token);
    }

    @GetMapping(path = "/getUnpassedExamsByStudent/")
    public List<ExamStatusInfoDTO> getUnpassedExamsByStudent(@RequestParam String token){
        return examStatusInfoService.getUnpassedExamsByStudent(token);
    }

    @PutMapping
    public ResponseEntity updateExamInfo(
            @RequestParam String index,
            @RequestParam Integer subjectID,
            Integer colloquiumOne,
            Integer colloquiumTwo,
            Integer colloquiumThree
    ) {
        examStatusInfoService.updateExamInfo(index, subjectID, colloquiumOne, colloquiumTwo, colloquiumThree);

        return ResponseEntity.ok(new MyCustomResponse("Uspesno promenjeni poeni za predispitne obaveze!"));
    }

    @PutMapping(path = "/passedExam/{token}")
    public ResponseEntity updatePassedExam(
            @PathVariable("token") String token,
            @RequestParam Integer subjectID,
            @RequestParam String index ,
            @RequestParam Integer examPoints,
            @RequestParam Integer eventID
    ) {
        examStatusInfoService.updateExamAsPassed(index, subjectID, token, eventID, examPoints);

        return ResponseEntity.ok(new MyCustomResponse("Uspesno promenjeno!"));
    }
}
