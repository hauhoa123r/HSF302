package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "physical_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PhysicalAssessmentEntity {
    @Column(name = "assessment_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_date")
    private Date assessmentDate;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "height")
    private Float height;

    @Column(name = "body_fat_percentage")
    private Float bodyFatPercentage;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;
}
