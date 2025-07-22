package com.web.api;

import com.web.model.response.PackageResponse;
import com.web.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PackageAPI {

    private PackageService packageService;

    @Autowired
    public void setPackageService(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping("/add")
    public void addPackage() {

    }

    @GetMapping("/api/package/{packageId}")
    public PackageResponse getPackageById(@PathVariable Long packageId) {
        return packageService.getPackage(packageId);
    }
}
