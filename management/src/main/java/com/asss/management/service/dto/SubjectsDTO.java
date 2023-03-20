package com.asss.management.service.dto;

import com.asss.management.entity.Enums.Course_of_studies;
import com.asss.management.entity.Enums.Semester;
import com.asss.management.entity.Enums.Year_of_studies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectsDTO {

    private Integer id;
    private String name;
    private Year_of_studies year;
    private Semester semester;
    private Course_of_studies course;
    private Integer espb;
}
