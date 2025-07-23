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
    private String brand;
    private String purchaseDate;
    private String location;
    private String imageUrl;
    private String code;
    private String nameCompany;
    private String email;
    private String phone;
    private String address;
    private Long purchasePrice;
    private Long warrantyPeriod;
    private String serialNumber;
    private String model;
}
