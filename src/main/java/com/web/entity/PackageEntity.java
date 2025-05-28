package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PackageEntity {
    @Column(name = "package_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Float price;

    @Column(name = "duration_days")
    private Long durationDays;

    @OneToMany(mappedBy = "packageEntity")
    private Set<PaymentEntity> paymentEntities;

    @OneToMany(mappedBy = "packageEntity")
    private Set<MemberPackageEntity> memberPackageEntities;
}
