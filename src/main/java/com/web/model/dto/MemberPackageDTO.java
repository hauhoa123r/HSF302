package com.web.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberPackageDTO {
    private Long id;
    private Date startDate;
    private Long memberEntityId;
    private Long packageEntityId;
    private Long promotionEntityId;
    @Pattern(regexp = "^(CASH|MOMO|TRANSFER)$")
    private String method;
}
