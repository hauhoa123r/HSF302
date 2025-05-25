package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
    private Long id;
    private String username;
    private String fullName;
    private String password;
    private String role;
    private String phone;
    private String email;
    private String dateOfBirth;
    private String gender;
    private String address;
}
