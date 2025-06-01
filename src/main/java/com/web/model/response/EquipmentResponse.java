package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EquipmentResponse {
    private Long id;
    private String name;
    private String type;
    private String status;
    private String lastMaintenance;

}
