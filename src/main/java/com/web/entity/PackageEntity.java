package com.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@FieldNameConstants
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
    private Set<MemberPackageEntity> memberPackageEntities;

    @Size(max = 50)
    @Column(name = "package_code", length = 50)
    private String packageCode;

    @Lob
    @Column(name = "package_type")
    private String packageType;

    @Lob
    @Column(name = "duration_unit")
    private String durationUnit;

    @Lob
    @Column(name = "status")
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Timestamp createdAt;

}
