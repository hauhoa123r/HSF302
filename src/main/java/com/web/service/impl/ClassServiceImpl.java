package com.web.service.impl;

import com.web.entity.ClassEntity;
import com.web.repository.ClassRepository;
import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassRepository classRepositoryImpl;

    @Override
    public ClassEntity getClassById(Long id) {
        return classRepositoryImpl.findById(id).orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
    }

    @Override
    public ClassEntity getClassByClassName(String className) {
        return classRepositoryImpl.findByClassNameContaining(className).orElseThrow(() -> new RuntimeException("Class not found with name: " + className));
    }

    @Override
    public void saveClass(ClassEntity classEntity) {
        classRepositoryImpl.save(classEntity);
    }

    @Override
    public void updateClass(ClassEntity classEntity) {classRepositoryImpl.save(classEntity); }

    @Override
    public void deleteClass(Long id) {
        classRepositoryImpl.deleteById(id);
    }
}
