package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.MessageEntity;
import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter {

    private ModelMapperConfig modelMapperConfig;

    @Autowired
    public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    public MessageEntity toEntity(MessageDTO messageDTO) {
        return modelMapperConfig.modelMapper().map(messageDTO, MessageEntity.class);
    }

    public MessageResponse toResponse(MessageEntity messageEntity) {
        return modelMapperConfig.modelMapper().map(messageEntity, MessageResponse.class);
    }
}
