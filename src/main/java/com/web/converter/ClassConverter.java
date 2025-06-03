package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.ClassEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.ClassDTO;
import com.web.model.response.ClassResponse;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClassConverter {

    private ModelMapperConfig modelMapperConfig;

    @Autowired
    public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    public ClassEntity toConverterClass(ClassDTO classDTO){
        Optional<ClassEntity> classEntityOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(classDTO, ClassEntity.class));
        return classEntityOptional.orElseThrow(() -> new ErrorMappingException(ClassDTO.class, ClassEntity.class));
    }

    public ClassResponse toConverterClassResponse(ClassEntity classEntity) {
        Optional<ClassResponse> classResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(classEntity, ClassResponse.class));
        return classResponseOptional.orElseThrow(() -> new ErrorMappingException(ClassEntity.class, ClassResponse.class));
    }
}
