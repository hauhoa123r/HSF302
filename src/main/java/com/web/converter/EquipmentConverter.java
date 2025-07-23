package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.EquipmentEntity;
import com.web.entity.SupplierEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class EquipmentConverter {

   @Autowired
   private ModelMapper modelMapper;

    public EquipmentEntity toEntity(EquipmentDTO equipmentDTO) {
       EquipmentEntity equipmentEntity = modelMapper.map(equipmentDTO, EquipmentEntity.class);
       return equipmentEntity;
    }


//    public EquipmentResponse toResponse(EquipmentEntity equipmentEntity) {
//        Optional<EquipmentResponse> equipmentResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(equipmentEntity, EquipmentResponse.class));
//        return equipmentResponseOptional.orElseThrow(() -> new ErrorMappingException(EquipmentEntity.class, EquipmentResponse.class));
//    }

    public EquipmentDTO toDTO(EquipmentEntity equipmentEntity) {
        EquipmentDTO equipmentDTO = modelMapper.map(equipmentEntity, EquipmentDTO.class);
        return equipmentDTO;
    }

    public EquipmentEntity toEntity(EquipmentDTO dto, SupplierEntity supplier) {
        EquipmentEntity entity = modelMapper.map(dto, EquipmentEntity.class);
        if (dto.getPurchaseDate() != null && !dto.getPurchaseDate().isEmpty()) {
            entity.setPurchaseDate(dto.getPurchaseDate());
        }
        entity.setSupplier(supplier);
        return entity;
    }

    public SupplierEntity toSupplierEntity(EquipmentDTO dto) {
        SupplierEntity supplier = modelMapper.map(dto, SupplierEntity.class);
        supplier.setCreatedAt(LocalDateTime.now());
        return supplier;
    }
}
