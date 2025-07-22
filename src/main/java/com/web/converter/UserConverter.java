package com.web.converter;

import com.web.entity.MemberEntity;
import com.web.entity.MemberPackageEntity;
import com.web.entity.PackageEntity;
import com.web.entity.UserEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.AddressDTO;
import com.web.model.dto.UserDTO;
import com.web.model.dto.UserRegisterDTO;
import com.web.model.response.UserLoginResponse;
import com.web.repository.MemberPackageRepository;
import com.web.repository.MemberRepository;
import com.web.repository.PackageRepository;
import com.web.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Component
@Transactional
public class UserConverter {

    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberPackageRepository memberPackageRepository;
    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserLoginResponse toConverterUserLogin(UserEntity userEntity) {
        UserLoginResponse userLoginResponse = modelMapper.map(userEntity, UserLoginResponse.class);
        return userLoginResponse;
    }


    public UserEntity toEntity(UserDTO userDTO) {
        return Optional.ofNullable(modelMapper.map(userDTO, UserEntity.class))
                .orElseThrow(() -> new ErrorMappingException(UserRegisterDTO.class, UserEntity.class));
    }

    private String toConverterAddress(AddressDTO addressDTO) {
        return addressDTO.getProvince() + ", " + addressDTO.getWard() + ", " + addressDTO.getDistrict();
    }

    public Boolean toConverterRegister(UserRegisterDTO userRegisterDTO) throws ParseException {
        SimpleDateFormat birthDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = birthDateFormat.parse(userRegisterDTO.getBirthDate());

        UserEntity userEntity = new UserEntity();
        userEntity.setFullName(userRegisterDTO.getFullName());
        userEntity.setEmail(userRegisterDTO.getEmail());
        userEntity.setPhone(userRegisterDTO.getPhone());
        userEntity.setDateOfBirth(birthDate);
        userEntity.setAddress(toConverterAddress(userRegisterDTO.getAddress()));
        userEntity.setCreatedAt(Date.from(Instant.now()));
        userEntity.setUsername(userRegisterDTO.getUsername());
        userEntity.setPassword(userRegisterDTO.getPassword());
        userEntity.setGender(userRegisterDTO.getGender());
        userEntity.setRole("MEMBER");
        userRepository.save(userEntity);
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUserEntity(userEntity);
        memberEntity.setIsActive(true);
        memberEntity.setMemberPackageEntities(new HashSet<>());
        PackageEntity packageEntity = packageRepository.findById(userRegisterDTO.getPackageId()).orElseThrow(() -> new ErrorMappingException(UserRegisterDTO.class, PackageEntity.class));
        MemberPackageEntity memberPackageEntity = new MemberPackageEntity();
        memberPackageEntity.setMemberEntity(memberEntity);
        memberPackageEntity.setPackageEntity(packageEntity);
        memberPackageEntity.setIsActive(true);
        Date now = Date.from(Instant.now());
        memberPackageEntity.setStartDate(new java.sql.Date(now.getTime()));
        DateTimeFormatter endDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localEndDate = LocalDate.parse(userRegisterDTO.getEndDate(), endDateFormat);
        memberPackageEntity.setEndDate(java.sql.Date.valueOf(localEndDate));
        memberPackageEntity.setFinalPrice(new BigDecimal(userRegisterDTO.getPrice()));
        memberEntity.getMemberPackageEntities().add(memberPackageEntity);
        memberRepository.save(memberEntity);

        return true;
    }


}
