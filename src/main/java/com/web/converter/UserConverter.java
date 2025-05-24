package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.UserEntity;
import com.web.model.response.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    @Autowired
    ModelMapperConfig modelMap;
    public UserLoginResponse toConverterUserLogin(UserEntity userEntity){
        UserLoginResponse userLoginResponse = modelMap.modelMapper().map(userEntity, UserLoginResponse.class);
        return userLoginResponse;
    }
}
