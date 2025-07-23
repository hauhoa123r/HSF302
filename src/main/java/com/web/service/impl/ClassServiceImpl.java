package com.web.service.impl;

import com.web.converter.ClassConverter;
import com.web.entity.ClassEntity;
import com.web.entity.ClassScheduleEntity;
import com.web.entity.TrainerEntity;
import com.web.entity.UserEntity;
import com.web.model.dto.ClassDTO;
import com.web.model.response.ClassResponse;
import com.web.repository.ClassEnrollmentRepository;
import com.web.repository.ClassRepository;
import com.web.repository.TrainerRepository;
import com.web.repository.UserRepository;
import com.web.service.ClassService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClassServiceImpl implements ClassService {
    private ClassRepository classRepository;
    private TrainerRepository trainerRepository;
    private ClassConverter classConverter;
    private ClassEnrollmentRepository classEnrollmentRepository;

    @Autowired
    public void setClassRepository(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @Autowired
    public void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setClassConverter(ClassConverter classConverter) {
        this.classConverter = classConverter;
    }

    @Autowired
    public void setClassEnrollmentRepository(ClassEnrollmentRepository classEnrollmentRepository) {
        this.classEnrollmentRepository = classEnrollmentRepository;
    }

    @Override
    public Long countClasses() {
        return classRepository.count();
    }

    @Override
    public Page<ClassDTO> getAllClasses(String clasName, Pageable pageable) {
        Page<ClassEntity> page;
        if (clasName != null && !clasName.isBlank()) {
            page = classRepository.findByClassNameContainingIgnoreCase(clasName, pageable);
        } else {
            page = classRepository.findAll(pageable);
        }
        return page.map(classConverter::toDto);
    }

    @Override
    public String deleteClass(Long id) {
        classRepository.deleteById(id);
        return "Deleted Class";
    }

    @Override
    public String addNewClass(ClassDTO dto) {
        ClassEntity classEntity = classConverter.toEntity(dto);
        classRepository.save(classEntity);
        return "Add Successfully";
    }
}


