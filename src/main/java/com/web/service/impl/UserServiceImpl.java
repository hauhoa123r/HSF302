package com.web.service.impl;

import com.web.converter.UserConverter;
import com.web.entity.UserEntity;
import com.web.exception.ResourceNotFoundException;
import com.web.model.dto.UserLoginDTO;
import com.web.model.dto.UserRegisterDTO;
import com.web.model.response.UserLoginResponse;
import com.web.repository.UserRepository;
import com.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepositoryImpl;

    @Autowired
    UserConverter userConverter;

    @Override
    public UserLoginResponse isLogin(UserLoginDTO userLoginDTO) {
        return Optional.ofNullable(userRepositoryImpl.findByUsernameAndPassword(userLoginDTO.getUsername(), userLoginDTO.getPassword()))
                .map(userConverter::toConverterUserLogin)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    @Override
    public UserEntity isRegister(UserRegisterDTO userRegisterDTO) {
        if (userRepositoryImpl.existsByUsername(userRegisterDTO.getUsername())) {
            throw new RuntimeException("Username exists");
        }

        if (userRepositoryImpl.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RuntimeException("Email exists");
        }

        UserEntity userEntity =userConverter.toConverterUserRegister(userRegisterDTO);
        userRepositoryImpl.save(userEntity);
        return userEntity;
    }
}
