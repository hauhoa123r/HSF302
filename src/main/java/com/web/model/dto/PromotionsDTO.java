package com.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionsDTO {
    private Long id;
    private String code;
    private String name;
    private String promotionType;
    private String discountDisplay;
    private Date startDate;
    private Date endDate;
    private String status;
    private Long remainingUsage;
}
