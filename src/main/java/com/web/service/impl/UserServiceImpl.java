package com.web.service.impl;

import com.web.converter.UserConverter;
import com.web.entity.UserEntity;
import com.web.model.dto.UserLoginDTO;
import com.web.model.response.UserLoginResponse;
import com.web.repository.UserRepository;
import com.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepositoryImpl;

    @Autowired
    UserConverter userConverter;
    @Override
    public UserLoginResponse isLogin(UserLoginDTO userLoginDTO) {
        UserEntity userEntity = userRepositoryImpl.findByUsernameAndPassword(userLoginDTO);
        UserLoginResponse userLoginResponse = userConverter.toConverterUserLogin(userEntity);
        return userLoginResponse;
    }
}
