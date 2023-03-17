package com.asss.management.service.dto;

import com.asss.management.entity.Employee;
import com.asss.management.entity.Enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Integer jmbg;
    private String email;
    private Gender gender;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthDate;
    private Employee_category employeeCategory;
    private Employee_status status;
    @JsonFormat(pattern = "dd-MM-yyyy HH-mm")
    private Date updatedAt;
    private Integer updatedBy;
    @JsonFormat(pattern = "dd-MM-yyyy HH-mm")
    private Date createdAt;
}
