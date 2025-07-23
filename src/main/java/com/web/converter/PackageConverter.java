package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.PackageEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PackageConverter {

    private ModelMapperConfig modelMapperConfig;

    @Autowired
    public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    public PackageResponse toResponse(PackageEntity packageEntity){
        Optional<PackageResponse> packageResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(packageEntity, PackageResponse.class));
        return packageResponseOptional.orElseThrow(() -> new ErrorMappingException(PackageEntity.class, PackageResponse.class));
    }

    public PackageEntity toEntity(PackageDTO packageDTO){
        Optional<PackageEntity> packageEntityOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(packageDTO, PackageEntity.class));
        return packageEntityOptional.orElseThrow(() -> new ErrorMappingException(PackageDTO.class, PackageEntity.class));
    }
}
