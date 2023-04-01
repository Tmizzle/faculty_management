package com.asss.management.service.dto;

import com.asss.management.entity.Enums.Currency;
import com.asss.management.entity.Enums.Finances_status;
import com.asss.management.entity.Exams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancesDTO {

    private Integer id;
    private StudentDTO student;
    private String note;
    private Currency currency;
    private double amount;
    private Finances_status status;
    private Date createdAt;
    private Exams idExam;
}
