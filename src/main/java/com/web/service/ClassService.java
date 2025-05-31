package com.web.service;

import com.web.entity.ClassEntity;
import com.web.model.dto.ClassDTO;

public interface ClassService {
    ClassEntity getClassById(Long id);

    ClassEntity getClassByClassName(String className);

    void saveClass(ClassDTO classDTO);

    void updateClass(ClassEntity classEntity);

    void deleteClass(Long id);

}
