package com.asss.management.service.implementation;

import com.asss.management.dao.ExamsRepo;
import com.asss.management.entity.Events;
import com.asss.management.entity.Exams;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.EventsDTO;
import com.asss.management.service.dto.ExamsDTO;
import com.asss.management.service.mapper.ExamsMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Data
public class ExamsService {

    private final ExamsRepo examsRepo;
    private final ExamsMapper examsMapper;
    private final JwtService jwtService;

    // Retrieves all exams registered
    public List<ExamsDTO> getExams() {
        List<Exams> examsList = examsRepo.findAll();
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    // Retrieves a exam entry by ID
    public ExamsDTO getExamEntryById(Integer id) {
        Exams exams = examsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "exam with id " + id + " does not exist"));
        ExamsDTO examsDTO = examsMapper.entityToDTO(exams);
        return examsDTO;
    }

    public List<ExamsDTO> getByStudentForEvent(String token, Integer eventID) {
        String userEmail = jwtService.extractUsername(token);
        List<Exams> examsList = examsRepo.findByStudentForEvent(userEmail, eventID);
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    public List<ExamsDTO> getByProfesorForEvent(String token, Integer eventID) {
        String userEmail = jwtService.extractUsername(token);
        List<Exams> examsList = examsRepo.findExamsForEventByProfesor(userEmail, eventID);
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    public List<ExamsDTO> getByEvent(Integer eventID) {
        List<Exams> examsList = examsRepo.findByEvent(eventID);
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }

    public List<ExamsDTO> getBySubjectForEvent(Integer subjectID, Integer eventID) {
        List<Exams> examsList = examsRepo.findBySubjectForEvent(subjectID, eventID);
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }
}
