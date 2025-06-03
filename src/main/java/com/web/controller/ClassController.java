package com.web.controller;

import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClassController {

    private ClassService classServiceImpl;

    @Autowired
    public void setClassServiceImpl(ClassService classServiceImpl) {
        this.classServiceImpl = classServiceImpl;
    }

    @GetMapping("/getAllClasses")
    public void getAllClasses() {
        classServiceImpl.getAllClasses();
    }
}
