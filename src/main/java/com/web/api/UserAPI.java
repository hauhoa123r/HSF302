package com.web.api;

import com.web.entity.UserEntity;
import com.web.model.dto.UserLoginDTO;
import com.web.model.dto.UserRegisterDTO;
import com.web.model.response.UserLoginResponse;
import com.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserAPI {

    @Autowired
    UserService userSericeImpl;

    @PostMapping
    public UserLoginResponse isLogin(@RequestBody UserLoginDTO userLoginDTO) {
        UserLoginResponse userLoginResponse = userSericeImpl.isLogin(userLoginDTO);
        return userLoginResponse;
    }
    @PostMapping("/register")
    public UserEntity isRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        UserEntity userEntity = userSericeImpl.isRegister(userRegisterDTO);
        return userEntity;
    }
}