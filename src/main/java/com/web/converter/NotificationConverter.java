package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.NotificationEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationConverter {

    private final ModelMapperConfig modelMapperConfig;

    public NotificationEntity toEntity(NotificationDTO notificationDTO) {
        Optional<NotificationEntity> notificationEntityOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(notificationDTO, NotificationEntity.class));
        return notificationEntityOptional.orElseThrow(() -> new ErrorMappingException(NotificationDTO.class, NotificationEntity.class));
    }

    public NotificationResponse toResponse(NotificationEntity notificationEntity) {
        Optional<NotificationResponse> notificationResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(notificationEntity, NotificationResponse.class));
        return notificationResponseOptional.orElseThrow(() -> new ErrorMappingException(NotificationEntity.class, NotificationResponse.class));
    }
}
