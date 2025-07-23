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
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class TrainerEntity {
    @Column(name = "trainer_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "biography")
    private String biography;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @OneToMany(mappedBy = "trainerEntity", fetch = FetchType.LAZY)
    private List<ClassEntity> classEntities;

    @OneToMany(mappedBy = "trainerEntity", fetch = FetchType.LAZY)
    private Set<PersonalBookingEntity> personalBookingEntities;

    @OneToMany(mappedBy = "trainerEntity", fetch = FetchType.LAZY)
    private List<MealTemplateEntity> mealTemplateEntities;

    @OneToMany(mappedBy = "trainerEntity", fetch = FetchType.LAZY)
    private List<WorkoutGoalEntity> workoutGoalEntities;

    @OneToMany(mappedBy = "trainerEntity", fetch = FetchType.LAZY)
    private List<MemberEntity> memberEntities;

    @Lob
    @Column(name = "specialization")
    private String specialization;

    @Lob
    @Column(name = "certificates")
    private String certificates;

    @Lob
    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "base_salary", precision = 10, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @ColumnDefault("0.00")
    @Column(name = "commission_rate", precision = 5, scale = 2)
    private BigDecimal commissionRate;

    @Lob
    @Column(name = "status")
    private String status;

    @Size(max = 100)
    @Column(name = "bank_account", length = 100)
    private String bankAccount;

    @Size(max = 50)
    @Column(name = "tax_code", length = 50)
    private String taxCode;

}
