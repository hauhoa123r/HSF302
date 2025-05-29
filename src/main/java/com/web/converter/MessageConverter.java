package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.MessageEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageConverter {

    private ModelMapperConfig modelMapperConfig;

    @Autowired
    public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    public MessageEntity toEntity(MessageDTO messageDTO) {
        Optional<MessageEntity> messageEntityOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(messageDTO, MessageEntity.class));
        return messageEntityOptional.orElseThrow(() -> new ErrorMappingException(MessageDTO.class, MessageEntity.class));
    }

    public MessageResponse toResponse(MessageEntity messageEntity) {
        Optional<MessageResponse> messageResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(messageEntity, MessageResponse.class));
        return messageResponseOptional.orElseThrow(() -> new ErrorMappingException(MessageEntity.class, MessageResponse.class));
    }
}
