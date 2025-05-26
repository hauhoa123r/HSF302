package com.web.service;

import com.web.entity.UserEntity;
import com.web.model.dto.UserLoginDTO;
import com.web.model.dto.UserRegisterDTO;
import com.web.model.response.UserLoginResponse;

public interface UserService {
    UserLoginResponse isLogin(UserLoginDTO userLoginDTO);
    UserEntity isRegister(UserRegisterDTO userRegisterDTO);
}
