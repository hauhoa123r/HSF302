package com.web.api;

import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/class")
public class ClassAPI {
    @Autowired
    private ClassService classService;

    public void addClass(){

    }
}
