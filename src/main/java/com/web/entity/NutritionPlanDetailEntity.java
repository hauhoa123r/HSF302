package com.web.entity;

import com.web.enums.MealStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "nutrition_plan_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutritionPlanDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nutrition_plan_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private NutritionPlanEntity nutritionPlan;

    @Column(name = "plan_date")
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MealStatus status;

    @Column(name = "calories")
    private Long calories;

    @Column(name = "protein", precision = 5, scale = 2)
    private BigDecimal protein;

    @Column(name = "carbs", precision = 5, scale = 2)
    private BigDecimal carbs;

    @Column(name = "fat", precision = 5, scale = 2)
    private BigDecimal fat;

    @Column(name = "fiber", precision = 5, scale = 2)
    private BigDecimal fiber;

    @Column(name = "water", precision = 5, scale = 2)
    private BigDecimal water;
}
