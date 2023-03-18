package com.asss.management.controller;

import com.asss.management.dao.SubjectsRepo;
import com.asss.management.entity.Enums.Semester;
import com.asss.management.entity.Enums.Year_of_studies;
import com.asss.management.entity.Subjects;
import com.asss.management.service.dto.SubjectsDTO;
import com.asss.management.service.implementation.SubjectsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/subjects")
@Data
@Tag(name = "Subjects API", description = "API for managing subjects")
@CrossOrigin(origins = "*")
public class SubjectsController {

    private final SubjectsService subjectsService;
    private final SubjectsRepo subjectsRepo;

    @GetMapping
    public List<SubjectsDTO> getSubjects(){
        return subjectsService.getSubjects();
    }

    @GetMapping(path = "/findByYear")
    public List<SubjectsDTO> getSubjectsByYear(@RequestParam String year){
        return subjectsService.getSubjectsByYear(year);
    }

    @GetMapping(path = "/findBySemester")
    public List<SubjectsDTO> getSubjectsBySemester(@RequestParam String semester){
        return subjectsService.getSubjectsBySemester(semester);
    }

    @PostMapping
    public ResponseEntity addNewSubject(@RequestBody Subjects subjects){
        subjectsService.addNewSubject(subjects);

        return ResponseEntity.ok("Added a new subject successfully");
    }
    @PutMapping(path = "{id}")
    public ResponseEntity updatedSubject(
            @PathVariable("id") Integer id,
            @Parameter(description = "Study year") @RequestParam(required = false) Year_of_studies year,
            @Parameter(description = "Study semester") @RequestParam(required = false) Semester semester
    ) {
        subjectsService.updateSubject(id, year, semester);

        return ResponseEntity.ok("Subject updated successfully");
    }

}
