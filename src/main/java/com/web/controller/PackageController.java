package com.web.controller;

import com.web.model.response.PackageResponse;
import com.web.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PackageController {

    private PackageService packageService;

    @Autowired
    public void setPackageService(PackageService packageService) {
        this.packageService = packageService;
    }

    public List<PackageResponse> getAllPackage() {
        return packageService.getAllPackages();
    }

    public String showPackagePageForAdmin() {
        return "admin/packages/list";
    }
}
