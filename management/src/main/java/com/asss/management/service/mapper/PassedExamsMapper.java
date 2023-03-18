package com.asss.management.service.mapper;

import com.asss.management.entity.PassedExams;
import com.asss.management.service.dto.PassedExamsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PassedExamsMapper {

    PassedExamsDTO entityToDTO (PassedExams passedExams);

    PassedExams DTOToEntity (PassedExamsDTO passedExamsDTO);

    List<PassedExamsDTO> entitiesToDTOs(List<PassedExams> passedExamsList);

    List<PassedExams> DTOsToEntities(List<PassedExamsDTO> passedExamsDTOList);
}
