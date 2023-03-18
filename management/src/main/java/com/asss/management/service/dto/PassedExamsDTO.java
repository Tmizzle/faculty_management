package com.asss.management.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassedExamsDTO {

    private Integer id;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "status", "updatedAt", "updatedBy", "createdAt", "createdBy"})
    private StudentDTO student;
    private SubjectsDTO subject;
    private Integer grade;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "status", "updatedAt", "updatedBy", "createdAt"})
    private EmployeeDTO profesor;
    private EventsDTO event;
}
