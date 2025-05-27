package com.web.service;

import com.web.entity.ClassEntity;

public interface ClassService {
    ClassEntity getClassById(Long id);

    ClassEntity getClassByClassName(String className);

    void saveClass(ClassEntity classEntity);

    void updateClass(ClassEntity classEntity);

    void deleteClass(Long id);

}
