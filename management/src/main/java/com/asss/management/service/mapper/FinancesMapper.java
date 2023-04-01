package com.asss.management.service.mapper;


import com.asss.management.entity.ExamStatusInfo;
import com.asss.management.entity.Finances;
import com.asss.management.service.dto.ExamStatusInfoDTO;
import com.asss.management.service.dto.FinancesDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FinancesMapper {

    FinancesDTO entityToDTO (Finances finances);

    Finances DTOToEntity (FinancesDTO financesDTO);

    List<FinancesDTO> entitiesToDTOs(List<Finances> financesList);

    List<Finances> DTOsToEntities(List<FinancesDTO> financesDTOList);
}
