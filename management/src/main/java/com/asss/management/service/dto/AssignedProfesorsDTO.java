package com.asss.management.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedProfesorsDTO {

    private Integer id;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "updatedAt", "updatedBy", "createdAt"})
    private EmployeeDTO profesor;
    private SubjectsDTO subject;
}
