package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.PackageEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;
import com.web.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PackageConverter {

    private ModelMapperConfig modelMapperConfig;
    private PackageRepository packageRepository;

    @Autowired
    public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    @Autowired
    public void setPackageRepository(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public PackageResponse toResponse(PackageEntity packageEntity) {
        Optional<PackageResponse> packageResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(packageEntity, PackageResponse.class));
        return packageResponseOptional.orElseThrow(() -> new ErrorMappingException(PackageEntity.class, PackageResponse.class));
    }

    public PackageEntity toEntity(PackageDTO packageDTO){
        Optional<PackageEntity> packageEntityOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(packageDTO, PackageEntity.class));
        return packageEntityOptional.orElseThrow(() -> new ErrorMappingException(PackageDTO.class, PackageEntity.class));
    }

    public PackageEntity toConverterPackageEntity(PackageDTO packageDTO) {
        Optional<PackageEntity> packageEntityOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(packageDTO, PackageEntity.class));
        return packageEntityOptional.orElseThrow(() -> new ErrorMappingException(PackageDTO.class, PackageEntity.class));
    }

    public List<PackageResponse> toConverterPackageResponse() {
        List<PackageEntity> packageEntityList = packageRepository.findAll();
        List<PackageResponse> packageResponses = new ArrayList<>();
        packageEntityList.forEach(packageEntity1 -> {
            PackageResponse packageResponse = modelMapperConfig.modelMapper().map(packageEntity1, PackageResponse.class);
            packageResponses.add(packageResponse);
        });
        return packageResponses;
    }
}
