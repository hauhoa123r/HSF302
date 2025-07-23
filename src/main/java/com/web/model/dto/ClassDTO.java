package com.web.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassDTO {
    private Long id;
    private String className;
    private String location;
    private int capacity;
    private String trainerName;
    private List<ClassScheduleDTO> schedules;
    private String classCode;
    private String classType;
    private String status;
    private String startTime;
    private String endTime;
    private Long trainerId;
    private String classLevel;
    private String description;
}

