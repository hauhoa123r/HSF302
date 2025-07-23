package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@FieldNameConstants
public class EquipmentEntity {
    @Column(name = "equipment_id")
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "last_maintenance")
    private Date lastMaintenance;
    @Column(name="brand")
    private String brand;
    @Column(name="purchase_date")
    private String purchaseDate;
    @Column(name = "location")
    private String location;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "code")
    private String code;
    @Column(name="model")
    private String model;
    @Column(name="serial_number")
    private String serialNumber;
    @Column(name="description")
    private String description;
    @Column(name = "purchase_price")
    private Long purchasePrice;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaintenanceRecordEntity> maintenanceRecords = new ArrayList<>();
}
