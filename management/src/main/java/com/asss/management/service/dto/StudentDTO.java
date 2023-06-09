package com.asss.management.service.dto;

import com.asss.management.entity.Enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Integer jmbg;
    private String email;
    private String index;
    private Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private Course_of_studies courseOfStudies;
    private Year_of_studies yearOfStudies;
    private Type_of_studies typeOfStudies;
    private Boolean renewed;
    private Student_status status;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date updatedAt;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "status", "updatedAt", "updatedBy", "createdAt"})
    private EmployeeDTO updatedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm")
    private Date createdAt;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "status", "updatedAt", "updatedBy", "createdAt"})
    private EmployeeDTO createdBy;
    private Boolean budget;
}
