package com.web.api;

import com.web.entity.ClassEntity;
import com.web.model.dto.ClassDTO;
import com.web.service.ClassEnrollmentService;
import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/class")
public class ClassAPI {

    @Autowired
    private ClassService classService;

    @Autowired
    private ClassEnrollmentService classEnrollmentServiceImpl;

    @PostMapping("/add")
    public void addClass(@RequestBody ClassDTO classDTO){
        classService.saveClass(classDTO);
    }

    @GetMapping("/joinClass")
    public void joinClass(@RequestParam Long classId, @RequestParam Long studentId) {
        classEnrollmentServiceImpl.enrollClass(classId, studentId);
    }

    @GetMapping("/leaveClass")
    public void leaveClass(@RequestParam Long classId, @RequestParam Long studentId) {
        classEnrollmentServiceImpl.unenrollClass(classId, studentId);
    }

}
