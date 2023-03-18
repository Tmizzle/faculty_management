package com.asss.management.service.mapper;

import com.asss.management.entity.Subjects;
import com.asss.management.service.dto.SubjectsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectsMapper {

    SubjectsDTO entityToDTO (Subjects subjects);

    Subjects DTOToEntity (SubjectsDTO subjectsDTO);

    List<SubjectsDTO> entitiesToDTOs(List<Subjects> subjectsList);

    List<Subjects> DTOsToEntities(List<SubjectsDTO> subjectsDTOList);
}
