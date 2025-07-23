package com.web.entity;

import com.web.enums.MealStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "nutrition_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@FieldNameConstants
public class NutritionPlanEntity {

    @Column(name = "plan_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_time")
    private int totalTime;

    @Column(name = "description")
    private String description;

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

    @Column(name = "created_at")
    private Date created_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @OneToMany(mappedBy = "nutritionPlan", fetch = FetchType.LAZY)
    private List<NutritionPlanDetailEntity> nutritionPlanDetailEntityList;

}
