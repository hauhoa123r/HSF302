package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
