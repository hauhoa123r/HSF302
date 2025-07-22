package com.web.converter;

import com.web.entity.UserEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.UserDTO;
import com.web.model.dto.UserRegisterDTO;
import com.web.model.response.UserLoginResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserConverter {

    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserLoginResponse toConverterUserLogin(UserEntity userEntity) {
        UserLoginResponse userLoginResponse = modelMapper.map(userEntity, UserLoginResponse.class);
        return userLoginResponse;
    }

    public UserEntity toConverterUserRegister(UserRegisterDTO userRegisterDTO) {
        UserEntity userEntity = modelMapper.map(userRegisterDTO, UserEntity.class);
        return userEntity;
    }

    public UserEntity toEntity(UserDTO userDTO) {
        return Optional.ofNullable(modelMapper.map(userDTO, UserEntity.class))
                .orElseThrow(() -> new ErrorMappingException(UserRegisterDTO.class, UserEntity.class));
    }
}
