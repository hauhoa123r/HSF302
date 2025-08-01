package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private UserResponse userEntity;
    private Long id;
    private Integer experienceYears;
    private String biography;
    private String status;
    private String memberCount;
    private String specialization;
    private String certificates;
    private String employmentType;
    private Date startDate;
    private BigDecimal baseSalary;
    private String bankAccount;
    private String taxCode;
}
