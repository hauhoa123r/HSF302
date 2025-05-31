package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.ClassEntity;
import com.web.model.dto.ClassDTO;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassConverter {

    @Autowired
    private ModelMapperConfig modelMapper;
    public ClassEntity toConverterClass(ClassDTO classDTO){
        return modelMapper.modelMapper().map(classDTO, ClassEntity.class);
    }
}
