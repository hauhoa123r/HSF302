package com.web.converter;

import com.web.entity.PromotionEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.response.PromotionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PromotionConverter {
    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PromotionResponse toResponse(PromotionEntity promotionEntity) {
        return Optional.ofNullable(modelMapper.map(promotionEntity, PromotionResponse.class))
                .orElseThrow(() -> new ErrorMappingException(PromotionEntity.class, PromotionResponse.class));
    }
}
