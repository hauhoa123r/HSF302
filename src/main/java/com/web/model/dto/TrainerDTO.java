package com.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    private Long id;
    private UserDTO userEntity;
    private String specialization;
    private Integer experienceYears;
    private String certificates;
    private String employmentType;
    private BigDecimal baseSalary;
    private String bankAccount;
    private String taxCode;
    private String status;
    private String sortFieldName;
    private String sortDirection;
}
