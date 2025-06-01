package com.web.converter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.config.ModelMapperConfig;
import com.web.entity.AttendanceEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;

@Component
public class AttendanceConverter {

    private ModelMapperConfig modelMapperConfig;

    @Autowired
    public void setAttendanceConverter(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    public AttendanceEntity toEntity(AttendanceDTO attendanceDTO) {
        Optional<AttendanceEntity> attendanceEntity = Optional.ofNullable(modelMapperConfig.modelMapper().map(attendanceDTO, AttendanceEntity.class));

        return attendanceEntity.orElseThrow(() -> new ErrorMappingException(AttendanceResponse.class, AttendanceEntity.class));
    }

    public AttendanceResponse toResponse(AttendanceEntity attendanceEntity) {
        Optional<AttendanceResponse> attendanceResponse = Optional.ofNullable(modelMapperConfig.modelMapper().map(attendanceEntity, AttendanceResponse.class));

        return attendanceResponse.orElseThrow(() -> new ErrorMappingException(AttendanceEntity.class, AttendanceResponse.class));
    }

}
