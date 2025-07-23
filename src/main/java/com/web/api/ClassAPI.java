package com.web.api;

import com.web.entity.ClassEntity;
import com.web.model.dto.ClassDTO;
import com.web.service.ClassEnrollmentService;
import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/class")
public class ClassAPI {
    @Autowired
    private ClassService classService;

    @PostMapping("/add")
    public ResponseEntity<?> addClass(@RequestBody ClassDTO dto) {
        classService.addNewClass(dto);
        return ResponseEntity.ok("Lớp học đã được tạo thành công!");
    }
}
