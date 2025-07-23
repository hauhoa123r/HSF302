package com.web.converter;

import com.web.entity.ClassEntity;
import com.web.entity.ClassScheduleEntity;
import com.web.entity.TrainerEntity;
import com.web.enums.ClassStatus;
import com.web.model.dto.ClassDTO;
import com.web.model.dto.ClassScheduleDTO;
import com.web.repository.ClassRepository;
import com.web.repository.TrainerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
public class ClassConverter {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private ClassRepository classRepository;
    public ClassDTO toDto(ClassEntity entity) {
        ClassDTO dto = modelMapper.map(entity, ClassDTO.class);

        if (entity.getTrainerEntity() != null) {
            dto.setTrainerName(entity.getTrainerEntity().getUserEntity().getFullName());
        }
        if (entity.getStatus() != null) {
            dto.setStatus(entity.getStatus().getDisplayName());
        }
        if (entity.getClassScheduleEntity() != null) {
            List<ClassScheduleDTO> scheduleDtos = entity.getClassScheduleEntity().stream()
                    .map(schedule -> modelMapper.map(schedule, ClassScheduleDTO.class))
                    .toList();
            dto.setSchedules(scheduleDtos);
        }

        return dto;
    }

    public ClassEntity toEntity(ClassDTO dto) {
        ClassEntity classEntity = modelMapper.map(dto, ClassEntity.class);
        classEntity.setStatus(ClassStatus.ACTIVE);
        long count = classRepository.count();
        int nextNumber = (int) count + 1;
        String classCode = String.format("CL%03d", nextNumber);
        classEntity.setClassCode(classCode);
        TrainerEntity trainer = trainerRepository.findById(dto.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer không tồn tại"));
        classEntity.setTrainerEntity(trainer);
        LocalTime start = LocalTime.parse(dto.getStartTime());
        LocalTime end = LocalTime.parse(dto.getEndTime());
        ClassScheduleEntity schedule = new ClassScheduleEntity();
        schedule.setStartTime(Timestamp.valueOf(start.atDate(LocalDate.now())));
        schedule.setEndTime(Timestamp.valueOf(end.atDate(LocalDate.now())));
        schedule.setClassEntity(classEntity);
        classEntity.setClassScheduleEntity(Set.of(schedule));
        return classEntity;
    }
}
