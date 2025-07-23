package com.web.service;

import com.web.entity.ClassEntity;
import com.web.model.dto.ClassDTO;
import com.web.model.response.ClassResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassService {
    Long countClasses();
    Page<ClassDTO> getAllClasses(String className, Pageable pageable);
    String deleteClass(Long id);
    String addNewClass(ClassDTO classDTO);
}
