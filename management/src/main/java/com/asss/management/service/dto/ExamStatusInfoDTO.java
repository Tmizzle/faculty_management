package com.asss.management.service.dto;

import com.asss.management.entity.Enums.Exam_status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamStatusInfoDTO {

    private Integer id;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "status", "updatedAt", "updatedBy", "createdAt", "createdBy"})
    private StudentDTO student;
    private SubjectsDTO subject;
    private Integer grade;
    @JsonIgnoreProperties(value = {"middleName", "jmbg", "gender", "birthDate", "status", "updatedAt", "updatedBy", "createdAt"})
    private EmployeeDTO profesor;
    private EventsDTO event;
    private Integer examPoints;
    private Integer ColloquiumOne;
    private Integer ColloquiumTwo;
    private Integer ColloquiumThree;
    private Exam_status status;
}
