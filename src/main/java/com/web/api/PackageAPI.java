package com.web.api;

import com.web.model.response.PackageResponse;
import com.web.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/package")
public class PackageAPI {

    @Autowired
    PackageService packageServiceImpl;

    @PostMapping("/add")
    public void addPackage() {

    }
}
