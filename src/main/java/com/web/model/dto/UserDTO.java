package com.web.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank
    private String fullName;
    private String username;
    @Email
    private String email;
    private String password;
    @Pattern(regexp = "^0\\d{9}$")
    private String phone;
    private String role;
    private Date createdAt;
    @Past
    private Date dateOfBirth;
    @Pattern(regexp = "^(MALE|FEMALE)$")
    private String gender;
    @NotBlank
    private String address;

    private String avatar;
    // Assuming idCard is a Vietnamese ID card number, 9 or 12 digits
    @Pattern(regexp = "^\\d{9}|\\d{12}$", message = "Số CMND/CCCD phải là 9 hoặc 12 chữ số")
    private String idCard;
    @NotBlank
    private String emergencyContact;
}
