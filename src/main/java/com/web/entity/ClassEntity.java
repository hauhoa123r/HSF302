package com.web.entity;

import com.web.enums.ClassStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
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
    @Column(name = "class_code")
    private String classCode;
    @Column(name = "class_type")
    private String classType;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ClassStatus status;
    @Column(name = "class_level")
    private String classLevel;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainerEntity;

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ClassEnrollmentEntity> classEnrollmentEntities;

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ClassScheduleEntity> classScheduleEntity;

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<BoxChatEntity> boxChatEntity;

}
