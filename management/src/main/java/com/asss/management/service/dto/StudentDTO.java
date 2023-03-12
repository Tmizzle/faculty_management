package com.asss.management.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Integer jmbg;
    private String index;
}
