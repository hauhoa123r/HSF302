package com.web.converter;

import com.web.entity.MemberEntity;
import com.web.entity.UserEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.MemberDTO;
import com.web.model.response.MemberHomePageResponse;
import com.web.model.response.MemberResponse;
import com.web.repository.MemberPackageRepository;
import com.web.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
public class MemberConverter {
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberPackageRepository memberPackageRepository;

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

    public MemberHomePageResponse toHomePageResponse(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ErrorMappingException(MemberDTO.class, UserEntity.class));
        MemberHomePageResponse memberHomePageResponse = new MemberHomePageResponse();
        memberHomePageResponse.setName(userEntity.getFullName());

        LocalDate start = LocalDate.now();

        LocalDate end = LocalDate.of(userEntity.getMemberEntity().getMemberPackageEntity().getEndDate().toLocalDate().getYear()
                , userEntity.getMemberEntity().getMemberPackageEntity().getEndDate().toLocalDate().getMonth()
                , userEntity.getMemberEntity().getMemberPackageEntity().getEndDate().toLocalDate().getDayOfMonth());

        memberHomePageResponse.setDateInMonth(ChronoUnit.DAYS.between(start, end));

        memberHomePageResponse.setClasses(memberPackageRepository.countByMemberEntity_Id(userEntity.getMemberEntity().getId()));

        return memberHomePageResponse;
    }
}
