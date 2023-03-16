package com.asss.management.service.implementation;

import com.asss.management.dao.EmployeeRepo;
import com.asss.management.entity.Employee;
import com.asss.management.service.dto.EmployeeDTO;
import com.asss.management.service.mapper.EmployeeMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;

    // Retrieves all employees
    public List<EmployeeDTO> getEmployee() {
        List<Employee> employeeList = employeeRepo.findAll();
        List<EmployeeDTO> employeeDTOList = employeeMapper.entitiesToDTOs(employeeList);
        return employeeDTOList;
    }
}
