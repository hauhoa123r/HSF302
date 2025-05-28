package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassEntity {
    @Column(name = "class_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "location")
    private String location;

    @Column(name = "capacity")
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainerEntity;

    @OneToMany(mappedBy = "class")
    private java.util.List<ClassEnrollmentEntity> classEnrollmentEntityList;

    @OneToMany(mappedBy = "classEntity")
    private Set<ClassScheduleEntity> classScheduleEntity;

    @OneToMany(mappedBy = "classEntity")
    private Set<BoxChatEntity> boxChatEntity;

}
