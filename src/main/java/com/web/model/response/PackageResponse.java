package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageResponse {
    private Long id;
    private String name;
    private String packageType;
    private String packageCode;
    private String description;
    private String price;
    private Long memberCount;
    private String status;
    private String durationDays;
}
