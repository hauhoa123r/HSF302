package com.web.converter;

import com.web.entity.TrainerEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.response.TrainerResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainerConverter {
    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TrainerResponse toResponse(TrainerEntity trainerEntity) {
        return Optional.ofNullable(modelMapper.map(trainerEntity, TrainerResponse.class))
                .orElseThrow(() -> new ErrorMappingException(TrainerEntity.class, TrainerResponse.class));
    }

}
