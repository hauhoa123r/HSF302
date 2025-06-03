package com.web.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassDTO {
    @NotBlank
    private String className;

    @NotBlank
    private String location;

    private int capacity;

    @NotNull
    private Long trainerId;
}

