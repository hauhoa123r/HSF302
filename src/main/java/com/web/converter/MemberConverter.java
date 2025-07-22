package com.web.converter;

import com.web.entity.MemberEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.MemberDTO;
import com.web.model.response.MemberResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MemberConverter {
    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MemberEntity toEntity(MemberDTO memberDTO) {
        return Optional.ofNullable(modelMapper.map(memberDTO, MemberEntity.class)).orElseThrow(
                () -> new ErrorMappingException(MemberDTO.class, MemberEntity.class));
    }

    public MemberResponse toResponse(MemberEntity memberEntity) {
        return Optional.ofNullable(modelMapper.map(memberEntity, MemberResponse.class)).orElseThrow(
                () -> new ErrorMappingException(MemberEntity.class, MemberResponse.class));
    }
}
