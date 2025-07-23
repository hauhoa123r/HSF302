package com.web.converter;

import com.web.entity.ClassEntity;
import com.web.entity.ClassScheduleEntity;
import com.web.model.response.RegisterClassResponse;
import com.web.repository.ClassRepository;
import com.web.repository.ClassScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RegisterClassConverter {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    public List<RegisterClassResponse> toConverterRegisterClassResponse() {
        List<ClassEntity> classEntityList = classRepository.findAll();
        List<RegisterClassResponse> registerClassResponses = new ArrayList<>();
        classEntityList.forEach(classEntity -> {
            RegisterClassResponse registerClassResponse = new RegisterClassResponse();
            registerClassResponse.setId(classEntity.getId());
            registerClassResponse.setName(classEntity.getClassName());
            registerClassResponse.setNameCoach(classEntity.getTrainerEntity().getUserEntity().getUsername());
            Optional<ClassScheduleEntity> classScheduleEntity = classScheduleRepository.findById(classEntity.getId());
            if (classScheduleEntity.isPresent() && classScheduleEntity.get().getStartTime() != null && classScheduleEntity.get().getEndTime() != null) {
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalDateTime start = classScheduleEntity.get().getStartTime().toLocalDateTime();
                LocalDateTime end = classScheduleEntity.get().getEndTime().toLocalDateTime();
                String formattedTime = timeFormat.format(start) + " - " + timeFormat.format(end);
                registerClassResponse.setDate(formattedTime);
            } else {
                registerClassResponse.setDate("Chưa đăng ký");
            }
            registerClassResponses.add(registerClassResponse);
        });
        return registerClassResponses;
    }
}
