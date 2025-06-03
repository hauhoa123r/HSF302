package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassResponse {
    private Long id;
    private String className;
    private String location;
    private int capacity;
    private String trainerName;
    private int memberCount;
    private String status; // full, not full;
}
