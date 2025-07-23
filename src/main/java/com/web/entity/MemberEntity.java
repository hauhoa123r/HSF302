package com.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "members")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class MemberEntity {
    @Column(name = "member_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(name = "qr_code", unique = true, length = 36)
    private String qrCode;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassEnrollmentEntity> classEnrollmentEntities;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonalBookingEntity> personalBookingEntities;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NutritionPlanEntity> nutritionPlanEntities;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhysicalAssessmentEntity> physicalAssessmentEntities;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkoutProgressEntity> workoutProgressEntities;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttendanceEntity> attendanceEntities;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessageEntity> messageEntities;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberPackageEntity> memberPackageEntities;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TrainerReviewEntity> trainerReviewEntities;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkoutGoalEntity> workoutGoalEntities = new HashSet<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_trainer_id")
    private TrainerEntity TrainerEntity;

    @Lob
    @Column(name = "note")
    private String note;

    @Size(max = 255)
    @Column(name = "occupation")
    private String occupation;

    @Size(max = 255)
    @Column(name = "hobby")
    private String hobby;

    public MemberPackageEntity getMemberPackageEntity() {
        if (memberPackageEntities == null || memberPackageEntities.isEmpty()) {
            return null;
        }
        for (MemberPackageEntity memberPackageEntity : memberPackageEntities) {
            if (memberPackageEntity.getIsActive()) {
                return memberPackageEntity;
            }
        }
        return null;
    }

    public Boolean getIsCheckedIn() {
        LocalDate today = LocalDate.now();
        if (attendanceEntities == null || attendanceEntities.isEmpty()) {
            return false;
        }
        return attendanceEntities.stream()
                .anyMatch(attendance -> attendance.getCheckInTime() != null &&
                        attendance.getCheckInTime().toLocalDateTime().toLocalDate().equals(today));
    }

    public void setIsCheckedIn(Boolean isCheckedIn) {
        // This method is not used in the current context, but can be implemented if needed.
    }

    public Boolean getIsCheckedOut() {
        LocalDate today = LocalDate.now();
        if (attendanceEntities == null || attendanceEntities.isEmpty()) {
            return false;
        }
        return attendanceEntities.stream()
                .anyMatch(attendance -> attendance.getCheckOutTime() != null &&
                        attendance.getCheckOutTime().toLocalDateTime().toLocalDate().equals(today));
    }

    public void setIsCheckedOut(Boolean isCheckedOut) {
        // This method is not used in the current context, but can be implemented if needed.
    }
}
