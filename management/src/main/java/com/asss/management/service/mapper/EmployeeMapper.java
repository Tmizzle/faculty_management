package com.asss.management.service.mapper;

import com.asss.management.entity.Employee;
import com.asss.management.service.dto.EmployeeDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDTO entityToDTO (Employee employee);

    Employee DTOToEntity (EmployeeDTO employeeDTO);

    List<EmployeeDTO> entitiesToDTOs(List<Employee> employeeList);

    List<Employee> DTOsToEntities(List<EmployeeDTO> employeeDTOList);
}
