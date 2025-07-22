package com.web.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String birthDate;
    @NotBlank
    private String gender;
    @NotBlank
    private AddressDTO address;
    @NotBlank
    private String phone;
    @NotBlank
    private Long packageId;
    @NotBlank
    private String endDate;
    @NotBlank
    private String price;
    @NotBlank
    private String fullName;
}
