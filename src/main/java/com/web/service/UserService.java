package com.web.service;

import com.web.model.dto.UserLoginDTO;
import com.web.model.dto.UserRegisterDTO;

import java.text.ParseException;

public interface UserService {
    Boolean isLogin(UserLoginDTO userLoginDTO);

    Boolean isVerifiedEmail(String email);

    Boolean isVerifiedPhone(String phone);

    Boolean isVerifiedUsername(String username);

    Boolean isRegister(UserRegisterDTO userRegisterDTO) throws ParseException;
}
