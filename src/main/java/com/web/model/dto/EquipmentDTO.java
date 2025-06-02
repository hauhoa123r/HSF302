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

public class EquipmentDTO {
    private Long id;
    private String name;
    private String type;
    private String status;
    private Date lastMaintenance;

}
