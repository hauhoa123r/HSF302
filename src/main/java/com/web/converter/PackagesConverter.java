package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.PackageEntity;
import com.web.model.response.PackagesReponse;
import com.web.repository.PackagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PackagesConverter {

    @Autowired
    ModelMapperConfig modelMapperConfig;
    @Autowired
    PackagesRepository packagesRepository;

    public PackagesReponse packagesReponse(Long id) {
        PackageEntity entity = packagesRepository.findById(id)
                .orElseThrow(null);
        return modelMapperConfig.modelMapper().map(entity, PackagesReponse.class);
    }
}
