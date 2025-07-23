package com.web.entity;

import com.web.enums.MealStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "meal_templates")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "name", length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private TrainerEntity trainerEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_status")
    private MealStatus mealStatus;

    @Column(name = "total_calories")
    private Integer totalCalories;

    @Column(name = "total_protein", precision = 6, scale = 2)
    private BigDecimal totalProtein;

    @Column(name = "total_carbs", precision = 6, scale = 2)
    private BigDecimal totalCarbs;

    @Column(name = "total_fat", precision = 6, scale = 2)
    private BigDecimal totalFat;

    @Column(name = "fiber", precision = 6, scale = 2)
    private BigDecimal fiber;

    @Column(name = "water", precision = 6, scale = 2)
    private BigDecimal water;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
