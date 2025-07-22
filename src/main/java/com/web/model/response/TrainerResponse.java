package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private UserResponse userEntity;
    private Long id;
    private Integer experienceYears;
    private String biography;
}
