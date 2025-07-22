package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String role;
    private Date createdAt;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String avatar;
    private String emergencyContact;
    private String idCard;
}
