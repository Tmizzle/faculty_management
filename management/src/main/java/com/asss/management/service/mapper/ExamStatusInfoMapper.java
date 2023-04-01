package com.asss.management.service.mapper;

import com.asss.management.entity.ExamStatusInfo;
import com.asss.management.service.dto.ExamStatusInfoDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamStatusInfoMapper {

    ExamStatusInfoDTO entityToDTO (ExamStatusInfo examStatusInfo);

    ExamStatusInfo DTOToEntity (ExamStatusInfoDTO examStatusInfoDTO);

    List<ExamStatusInfoDTO> entitiesToDTOs(List<ExamStatusInfo> examStatusInfoList);

    List<ExamStatusInfo> DTOsToEntities(List<ExamStatusInfoDTO> examStatusInfoDTOList);
}
