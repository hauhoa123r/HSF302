package com.web.entity;

import jakarta.persistence.*;
import lombok.experimental.FieldNameConstants;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "class_schedules")
@FieldNameConstants
public class ClassScheduleEntity {
    @Column(name = "schedule_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @Column(name = "schedule_date")
    private Date scheduleDate;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "location")
    private String location;
}
