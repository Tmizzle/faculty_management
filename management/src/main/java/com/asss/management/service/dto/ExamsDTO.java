package com.asss.management.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamsDTO {

    private Integer id;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "updatedAt", "updatedBy", "createdAt"})
    private EmployeeDTO profesor;
    private SubjectsDTO subject;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "renewed", "updatedAt", "updatedBy", "createdAt"})
    private StudentDTO student;
    private EventsDTO event;
    private Date createdAt;
}
