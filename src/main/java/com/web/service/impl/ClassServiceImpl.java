package com.web.service.impl;

import com.web.converter.ClassConverter;
import com.web.entity.ClassEntity;
import com.web.entity.TrainerEntity;
import com.web.entity.UserEntity;
import com.web.model.dto.ClassDTO;
import com.web.model.response.ClassResponse;
import com.web.repository.ClassEnrollmentRepository;
import com.web.repository.ClassRepository;
import com.web.repository.TrainerRepository;
import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassRepository classRepositoryImpl;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassConverter classConverter;

    @Autowired
    private ClassEnrollmentRepository classEnrollmentRepositoryImpl;

    @Override
    public ClassEntity getClassById(Long id) {
        return classRepositoryImpl.findById(id).orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
    }

    @Override
    public ClassEntity getClassByClassName(String className) {
        return classRepositoryImpl.findByClassNameContaining(className).orElseThrow(() -> new RuntimeException("Class not found with name: " + className));
    }


    @Override
    public void saveClass(ClassDTO classDTO) {

        ClassEntity classEntity = classConverter.toConverterClass(classDTO);

        TrainerEntity trainer = trainerRepository.findById(classDTO.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + classDTO.getTrainerId()));

        classEntity.setTrainerEntity(trainer);

        classRepository.save(classEntity);
    }

    @Override
    public void updateClass(ClassEntity classEntity) {classRepositoryImpl.save(classEntity); }

    @Override
    public void deleteClass(Long id) {
        classRepositoryImpl.deleteById(id);
    }

    @Override
    public List<ClassResponse> getAllClasses() {
        List<ClassResponse> classResponses = new ArrayList<>();
        List<ClassEntity> classEntities = classRepositoryImpl.findAll();

        for (ClassEntity classEntity : classEntities) {
            ClassResponse classResponse = new ClassResponse();
            TrainerEntity trainerEntity = trainerRepository.findById(classEntity.getTrainerEntity().getId()).orElseThrow(() -> new RuntimeException("Trainer not found with id: " + classEntity.getTrainerEntity().getId()));
            UserEntity userEntity = trainerEntity.getUserEntity();
            int memberCount = classEnrollmentRepositoryImpl.countByClassEntity(classEntity);
            classResponse.setId(classEntity.getId());
            classResponse.setClassName(classEntity.getClassName());
            classResponse.setCapacity(classEntity.getCapacity());
            classResponse.setLocation(classEntity.getLocation());
            classResponse.setTrainerName(userEntity.getFullName());
            classResponse.setMemberCount(memberCount);
            if (memberCount < classEntity.getCapacity()) {
                classResponse.setStatus("Available");
            } else {
                classResponse.setStatus("Full");
            }
            classResponses.add(classResponse);

        }
        return classResponses;
    }

}


