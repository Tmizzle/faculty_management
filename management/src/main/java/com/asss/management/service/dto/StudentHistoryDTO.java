package com.asss.management.service.dto;

import com.asss.management.entity.Enums.Year_of_studies;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentHistoryDTO {

    private Integer id;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "status", "updatedAt", "updatedBy", "createdAt", "createdBy"})
    private StudentDTO student;
    private Year_of_studies yearOfStudies;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date createdAt;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "status", "updatedAt", "updatedBy", "createdAt"})
    private EmployeeDTO createdBy;
    private Boolean budget;
}
