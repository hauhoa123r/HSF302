package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String qrCode;
    private Boolean isActive;
    private String note;
    private String occupation;
    private String hobby;
    private Boolean isCheckedIn;
    private Boolean isCheckedOut;
    private UserResponse userEntity;
    private TrainerResponse trainerEntity;
    private MemberPackageResponse memberPackageEntity;
}
