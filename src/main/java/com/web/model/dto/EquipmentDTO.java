package com.web.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EquipmentDTO {
    private Long id;
    private String name;
    private String type;
    private String status;
    private String lastMaintenance;

}
