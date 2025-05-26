package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "class_enrollments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enrollment_date")
    private Timestamp enrollmentDate;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;
}
