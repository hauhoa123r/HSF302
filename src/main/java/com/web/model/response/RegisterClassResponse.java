package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClassResponse {
    private Long id;
    private String name;
    private String date = "";
    private String nameCoach;

}
