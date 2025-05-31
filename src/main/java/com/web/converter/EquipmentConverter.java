package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.EquipmentEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EquipmentConverter {

    private ModelMapperConfig modelMapperConfig;

    @Autowired
    public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    public EquipmentEntity toEntity(EquipmentDTO equipmentDTO) {
        Optional<EquipmentEntity> equipmentEntityOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(equipmentDTO, EquipmentEntity.class));
        return equipmentEntityOptional.orElseThrow(() -> new ErrorMappingException(EquipmentDTO.class, EquipmentEntity.class));
    }

    public EquipmentResponse toResponse(EquipmentEntity equipmentEntity) {
        Optional<EquipmentResponse> equipmentResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(equipmentEntity, EquipmentResponse.class));
        return equipmentResponseOptional.orElseThrow(() -> new ErrorMappingException(EquipmentEntity.class, EquipmentResponse.class));
    }
}
