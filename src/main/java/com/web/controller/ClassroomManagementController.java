package com.web.controller;


import com.web.entity.UserEntity;
import com.web.model.dto.ClassDTO;
import com.web.repository.UserRepository;
import com.web.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ClassroomManagementController {

    @Autowired
    private UserRepository userRepository;
    private ClassService classService;

    @Autowired
    public void setClassServiceImpl(ClassService classService) {
        this.classService = classService;
    }
    @GetMapping("/admin/getAllClasses")
    public String listClasses(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,  @RequestParam(required = false) String className) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassDTO> classPage = classService.getAllClasses(className, pageable);
        model.addAttribute("classPage", classPage);
        model.addAttribute("keyword", className);
        return"admin/classes/list";
    }

    @GetMapping("/admin/addClass")
    public String addClass(Model model){
        List<UserEntity> trainers = userRepository.findByRole("TRAINER");
        model.addAttribute("trainers", trainers);
        return "admin/classes/add";
    }

    @GetMapping("/admin/delete")
    public String deleteClass(@RequestParam Long id){
        classService.deleteClass(id);
        return "redirect:/admin/getAllClasses";
    }
}
