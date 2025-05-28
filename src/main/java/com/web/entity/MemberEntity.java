package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "members")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity {
    @Column(name = "member_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qr_code")
    private String qrCode;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "memberEntity")
    private List<ClassEnrollmentEntity> classEnrollmentEntityList;

    @OneToMany(mappedBy = "memberEntity")
    private Set<PersonalBookingEntity> personalBookingEntities;

    @OneToMany(mappedBy = "memberEntity")
    private Set<NutritionPlanEntity> nutritionPlanEntities;

    @OneToMany(mappedBy = "memberEntity")
    private Set<PhysicalAssessmentEntity> physicalAssessmentEntities;

    @OneToMany(mappedBy = "memberEntity")
    private Set<WorkoutProgressEntity> workoutProgressEntities;

    @OneToMany(mappedBy = "memberEntity")
    private Set<AttendanceEntity> attendanceEntities;

    @OneToMany(mappedBy = "memberEntity")
    private Set<MessageEntity> messageEntities;

    @OneToMany(mappedBy = "memberEntity")
    private Set<PaymentEntity> paymentEntities;

    @OneToMany(mappedBy = "memberEntity")
    private Set<MemberPackageEntity> memberPackageEntities;
}
