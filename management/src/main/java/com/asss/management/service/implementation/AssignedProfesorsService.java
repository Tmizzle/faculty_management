package com.asss.management.service.implementation;

import com.asss.management.dao.AssignedProfesorsRepo;
import com.asss.management.dao.EmployeeRepo;
import com.asss.management.dao.SubjectsRepo;
import com.asss.management.entity.AssignedProfesors;
import com.asss.management.entity.Employee;
import com.asss.management.entity.Subjects;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.AssignedProfesorsDTO;
import com.asss.management.service.mapper.AssignedProfesorsMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.List;

@Service
@Data
public class AssignedProfesorsService {

    private final AssignedProfesorsRepo assignedProfesorsRepo;
    private final AssignedProfesorsMapper assignedProfesorsMapper;
    private final EmployeeRepo employeeRepo;
    private final SubjectsRepo subjectsRepo;
    private final JwtService jwtService;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Retrieves all assigned profesors
    public List<AssignedProfesorsDTO> getAssignedProfesors() {
        List<AssignedProfesors> assignedProfesorsList = assignedProfesorsRepo.findAll();
        List<AssignedProfesorsDTO> assignedProfesorsDTOList = assignedProfesorsMapper.entitiesToDTOs(assignedProfesorsList);
        return assignedProfesorsDTOList;
    }

    public List<AssignedProfesorsDTO> getAssignedSubjectsForLoggedUser(String token) {
        String userEmail = jwtService.extractUsername(token);
        List<AssignedProfesors> assignedProfesorsList = assignedProfesorsRepo.findByProfesor(userEmail);
        List<AssignedProfesorsDTO> assignedProfesorsDTOList = assignedProfesorsMapper.entitiesToDTOs(assignedProfesorsList);
        return assignedProfesorsDTOList;
    }

    public List<AssignedProfesorsDTO> getAssignedProfesorsBySubject(Integer subjectID) {
        List<AssignedProfesors> assignedProfesorsList = assignedProfesorsRepo.findBySubject(subjectID);
        List<AssignedProfesorsDTO> assignedProfesorsDTOList = assignedProfesorsMapper.entitiesToDTOs(assignedProfesorsList);
        return assignedProfesorsDTOList;
    }

    public AssignedProfesorsDTO getAssignedProfesorsById(Integer id) {
        AssignedProfesors assignedProfesor = assignedProfesorsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified employee has not been found."));
        AssignedProfesorsDTO assignedProfesorDTO = assignedProfesorsMapper.entityToDTO(assignedProfesor);
        return assignedProfesorDTO;
    }

    public void addNewAssignedProfesor(Integer profesorID, Integer subjectID) {
        Employee profesor = employeeRepo.findById(profesorID).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified employee has not been found."));
        Subjects subject = subjectsRepo.findById(subjectID).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified subject has not been found."));

        AssignedProfesors checkIfAlreadyAssigned = assignedProfesorsRepo.findIfAlreadyAssigned(profesorID, subjectID);

        if(checkIfAlreadyAssigned != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Profesor already assigned to that subject");
        }

        AssignedProfesors newAssignedProfesor = new AssignedProfesors();

        newAssignedProfesor.setProfesor(profesor);
        newAssignedProfesor.setSubject(subject);
        assignedProfesorsRepo.save(newAssignedProfesor);
    }
    public void deleteAssignedProfesor(Integer profesorID, Integer subjectID) {
        AssignedProfesors assignedProfesor = assignedProfesorsRepo.findIfAlreadyAssigned(profesorID, subjectID);
        if(assignedProfesor == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry with those params not found");
        }
        assignedProfesorsRepo.delete(assignedProfesor);
    }
}
