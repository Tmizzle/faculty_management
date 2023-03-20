package com.asss.management.service.mapper;

import com.asss.management.entity.Exams;
import com.asss.management.service.dto.ExamsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamsMapper {

    ExamsDTO entityToDTO (Exams exams);

    Exams DTOToEntity (ExamsDTO examsDTO);

    List<ExamsDTO> entitiesToDTOs(List<Exams> examsList);

    List<Exams> DTOsToEntities(List<ExamsDTO> examsDTOList);
}
