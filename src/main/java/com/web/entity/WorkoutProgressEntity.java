package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "workout_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WorkoutProgressEntity {
    @Column(name = "progress_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_date")
    private Date trainingDate;

    @Column(name = "exercise")
    private String exercise;

    @Column(name = "sets")
    private Long sets;

    @Column(name = "reps")
    private Long reps;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;
}
