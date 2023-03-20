package com.asss.management.service.mapper;

import com.asss.management.entity.AssignedProfesors;
import com.asss.management.entity.Employee;
import com.asss.management.service.dto.AssignedProfesorsDTO;
import com.asss.management.service.dto.EmployeeDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssignedProfesorsMapper {

    AssignedProfesorsDTO entityToDTO (AssignedProfesors assignedProfesors);

    AssignedProfesors DTOToEntity (AssignedProfesorsDTO assignedProfesorsDTO);

    List<AssignedProfesorsDTO> entitiesToDTOs(List<AssignedProfesors> assignedProfesorsList);

    List<AssignedProfesors> DTOsToEntities(List<AssignedProfesorsDTO> assignedProfesorsDTOList);
}
