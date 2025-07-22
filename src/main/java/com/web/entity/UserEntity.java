package com.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class UserEntity {
    @Column(name = "user_id")
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "avatar")
    private String avatar;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberEntity memberEntity;

    @OneToOne(mappedBy = "userEntity")
    private TrainerEntity trainerEntity;

    @OneToMany(mappedBy = "userEntity")
    private Set<NotificationEntity> notificationEntities;

    @Size(max = 20)
    @Column(name = "id_card", length = 20)
    private String idCard;

    @Size(max = 255)
    @Column(name = "emergency_contact")
    private String emergencyContact;

}
