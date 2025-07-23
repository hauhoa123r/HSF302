package com.web.service;

import com.web.entity.UserEntity;
import com.web.model.dto.ResetPasswordDTO;
import com.web.model.dto.UserLoginDTO;
import com.web.model.dto.UserRegisterDTO;

import java.text.ParseException;

public interface UserService {
    UserEntity isLogin(UserLoginDTO userLoginDTO);

    Boolean isVerifiedEmail(String email);

    Boolean isVerifiedPhone(String phone);

    Boolean isVerifiedUsername(String username);

    Boolean isRegister(UserRegisterDTO userRegisterDTO) throws ParseException;

    Boolean isResetPassword(ResetPasswordDTO resetPasswordDTO);
}
