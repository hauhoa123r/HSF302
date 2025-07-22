package com.web.service;

import com.web.entity.ClassEntity;
import com.web.model.dto.ClassDTO;
import com.web.model.response.ClassResponse;

import java.util.List;

public interface ClassService {
    ClassEntity getClassById(Long id);

    ClassEntity getClassByClassName(String className);

    void saveClass(ClassDTO classDTO);

    void updateClass(ClassEntity classEntity);

    void deleteClass(Long id);

    List<ClassResponse> getAllClasses();

    Long countClasses();
}
