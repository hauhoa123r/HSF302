package com.web.converter;

import com.web.entity.MemberPackageEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.MemberPackageDTO;
import com.web.model.response.MemberPackageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MemberPackageConverter {
    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MemberPackageEntity toEntity(MemberPackageDTO memberPackageDTO) {
        return Optional.ofNullable(modelMapper.map(memberPackageDTO, MemberPackageEntity.class)
        ).orElseThrow(() -> new ErrorMappingException(MemberPackageDTO.class, MemberPackageEntity.class));
    }

    public MemberPackageResponse toResponse(MemberPackageEntity memberPackageEntity) {
        return Optional.ofNullable(modelMapper.map(memberPackageEntity, MemberPackageResponse.class)
        ).orElseThrow(() -> new ErrorMappingException(MemberPackageEntity.class, MemberPackageResponse.class));
    }
}
