package com.web.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EquipmentResponse {
    private Long id;
    private String name;
    private String type;
    private String status;
    private String lastMaintenance;

}
