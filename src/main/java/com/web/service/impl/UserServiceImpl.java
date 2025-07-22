package com.web.service.impl;

import com.web.converter.UserConverter;
import com.web.entity.UserEntity;
import com.web.model.dto.ResetPasswordDTO;
import com.web.model.dto.UserLoginDTO;
import com.web.model.dto.UserRegisterDTO;
import com.web.repository.UserRepository;
import com.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepositoryImpl;

    @Autowired
    UserConverter userConverter;

    @Override
    public UserEntity isLogin(UserLoginDTO userLoginDTO) {
        UserEntity userEntity = userRepositoryImpl.findByUsernameAndPassword(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        return userEntity;
    }

    @Override
    public Boolean isVerifiedEmail(String email) {
        return userRepositoryImpl.existsByEmail(email);
    }

    @Override
    public Boolean isVerifiedPhone(String phone) {
        return userRepositoryImpl.existsByPhone(phone);
    }

    @Override
    public Boolean isVerifiedUsername(String username) {
        return userRepositoryImpl.existsByUsername(username);
    }

    @Override
    public Boolean isRegister(UserRegisterDTO userRegisterDTO) throws ParseException {
        Boolean isRegister = userConverter.toConverterRegister(userRegisterDTO);
        return true;
    }

    @Override
    public Boolean isResetPassword(ResetPasswordDTO resetPasswordDTO) {
        UserEntity userEntity = userRepositoryImpl.findByEmail(resetPasswordDTO.getEmail());
        if (userEntity == null) {
            throw new RuntimeException("User not found with email: " + resetPasswordDTO.getEmail());
        }
        userEntity.setPassword(resetPasswordDTO.getPassword());
        userRepositoryImpl.save(userEntity);
        return true;
    }
}
