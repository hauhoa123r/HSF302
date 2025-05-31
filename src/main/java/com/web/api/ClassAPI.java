package com.web.api;

import com.web.entity.ClassEntity;
import com.web.model.dto.ClassDTO;
import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassAPI {
    @Autowired
    private ClassService classService;

    @PostMapping("/add")
    public void addClass(@RequestBody ClassDTO classDTO){
        classService.saveClass(classDTO);
    }


}
