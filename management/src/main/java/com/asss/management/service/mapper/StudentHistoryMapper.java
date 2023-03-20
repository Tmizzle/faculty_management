package com.asss.management.service.mapper;

import com.asss.management.entity.StudentHistory;
import com.asss.management.service.dto.StudentHistoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentHistoryMapper {

    StudentHistoryDTO entityToDTO (StudentHistory studentHistory);

    StudentHistory DTOToEntity (StudentHistoryDTO studentHistoryDTO);

    List<StudentHistoryDTO> entitiesToDTOs(List<StudentHistory> studentHistoryList);

    List<StudentHistory> DTOsToEntities(List<StudentHistoryDTO> studentHistoryDTOList);
}
