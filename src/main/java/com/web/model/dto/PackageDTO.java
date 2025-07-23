package com.web.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PackageDTO {
    @NotBlank
    private String name;
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$")
    private String status;
    @NotBlank
    private String packageCode;
    @Pattern(regexp = "^(MONTHLY|WEEKLY|YEARLY|DAILY)$")
    private String packageType;
    private String description;
    @Positive
    private Float price;
    @Positive
    private Integer durationDays;
    private String sortFieldName;
    private String sortDirection;
}
