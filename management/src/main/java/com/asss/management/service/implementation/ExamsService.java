package com.asss.management.service.implementation;

import com.asss.management.dao.ExamsRepo;
import com.asss.management.entity.Exams;
import com.asss.management.service.dto.ExamsDTO;
import com.asss.management.service.mapper.ExamsMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ExamsService {

    private final ExamsRepo examsRepo;
    private final ExamsMapper examsMapper;

    // Retrieves all exams registered
    public List<ExamsDTO> getExams() {
        List<Exams> examsList = examsRepo.findAll();
        List<ExamsDTO> examsDTOList = examsMapper.entitiesToDTOs(examsList);
        return examsDTOList;
    }
}
