package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPackageResponse {
    private Long id;
    private Date startDate;
    private Date endDate;
    private Boolean isActive;
    private PackageResponse packageEntity;
    private PromotionResponse promotionEntity;
}
