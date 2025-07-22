package com.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@FieldNameConstants
public class PromotionEntity {
    @Column(name = "promotion_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_percent")
    private Float discountPercent;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "promotionEntity")
    private Set<AppliedPromotionEntity> appliedPromotionEntity;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 50)
    @Column(name = "promotion_type", length = 50)
    private String promotionType;

    @Size(max = 50)
    @Column(name = "discount_type", length = 50)
    private String discountType;

    @Column(name = "minimum_amount", precision = 10, scale = 2)
    private BigDecimal minimumAmount;

    @Column(name = "maximum_discount", precision = 10, scale = 2)
    private BigDecimal maximumDiscount;

    @Column(name = "max_usage")
    private Long maxUsage;

    @ColumnDefault("1")
    @Column(name = "usage_per_customer")
    private Long usagePerCustomer;

    @ColumnDefault("0")
    @Column(name = "remaining_usage")
    private Long remainingUsage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private PackageEntity packageEntity;

    @Lob
    @Column(name = "status")
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Timestamp createdAt;

}
