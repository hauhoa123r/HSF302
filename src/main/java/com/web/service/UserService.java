package com.web.service;

import com.web.model.dto.UserLoginDTO;
import com.web.model.response.UserLoginResponse;

public interface UserService {
    UserLoginResponse isLogin(UserLoginDTO userLoginDTO);
}
