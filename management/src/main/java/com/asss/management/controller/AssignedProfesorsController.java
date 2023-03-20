package com.asss.management.controller;

import com.asss.management.service.dto.AssignedProfesorsDTO;
import com.asss.management.service.implementation.AssignedProfesorsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/assigned_profesors")
@Data
@Tag(name = "Assigned profesors API", description = "API for managing assigned profesors")
@CrossOrigin(origins = "*")
public class AssignedProfesorsController {

    private final AssignedProfesorsService assignedProfesorsService;

    @GetMapping
    public List<AssignedProfesorsDTO> getAssignedProfesors(){
        return assignedProfesorsService.getAssignedProfesors();
    }

    @GetMapping(path = "/getAssignedSubjectsByLoggedProfesor/")
    public List<AssignedProfesorsDTO> getAssignedSubjectsByLoggedProfesor(@RequestParam String token){
        return assignedProfesorsService.getAssignedSubjectsForLoggedUser(token);
    }

    @GetMapping(path = "/getAssignedProfesorsBySubject/{id}")
    public List<AssignedProfesorsDTO> getAssignedProfesorsBySubject(@PathVariable("id") Integer subjectID){
        return assignedProfesorsService.getAssignedProfesorsBySubject(subjectID);
    }

    @GetMapping(path = "{id}")
    public AssignedProfesorsDTO getById(@PathVariable("id") Integer id){
        return assignedProfesorsService.getAssignedProfesorsById(id);
    }

    @PostMapping
    public ResponseEntity addNewAssignedProfesor(@RequestParam Integer profesorID, @RequestParam Integer subjectID){
        assignedProfesorsService.addNewAssignedProfesor(profesorID, subjectID);

        return ResponseEntity.ok("Added a new assigned profesor successfully");
    }

    @DeleteMapping
    public ResponseEntity deleteProjectMember(@RequestParam Integer profesorID, @RequestParam Integer subjectID ){
        assignedProfesorsService.deleteAssignedProfesor(profesorID, subjectID);

        return ResponseEntity.ok("Assigned profesor successfully removed from that project");
    }
}
