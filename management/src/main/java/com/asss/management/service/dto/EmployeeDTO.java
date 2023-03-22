package com.asss.management.service.dto;

import com.asss.management.entity.Employee;
import com.asss.management.entity.Enums.*;
import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties(value = {"accountNonLocked", "enabled", "accountNonExpired", "credentialsNonExpired", "username", "authorities", "middleName", "jmbg", "gender", "birthDate", "password", "oldPassword", "status", "updatedAt", "updatedBy", "createdAt"})
    private Employee updatedBy;
    @JsonFormat(pattern = "dd-MM-yyyy HH-mm")
    private Date createdAt;
}
