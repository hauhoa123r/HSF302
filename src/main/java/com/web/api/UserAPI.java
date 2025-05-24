package com.web.api;

import com.web.model.dto.UserLoginDTO;
import com.web.model.response.UserLoginResponse;
import com.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserAPI {

    @Autowired
    UserService userSericeImpl;

    @PostMapping
    public UserLoginResponse isLogin(@ModelAttribute UserLoginDTO userLoginDTO) {
        UserLoginResponse userLoginResponse = userSericeImpl.isLogin(userLoginDTO);
        return userLoginResponse;
    }
}