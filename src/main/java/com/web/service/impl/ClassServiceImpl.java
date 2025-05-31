package com.web.service.impl;

import com.web.converter.ClassConverter;
import com.web.entity.ClassEntity;
import com.web.entity.TrainerEntity;
import com.web.model.dto.ClassDTO;
import com.web.repository.ClassRepository;
import com.web.repository.TrainerRepository;
import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

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

    public String classReflection(ClassDTO classDTO) {
        StringBuilder result = new StringBuilder();
        Field[] fields = ClassDTO.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(classDTO);
                if (value != null) {
                    String key = field.getName();
                    result.append(key).append("=").append(value.toString()).append("&");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        if (result.length() > 0 && result.charAt(result.length() - 1) == '&') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }
}


