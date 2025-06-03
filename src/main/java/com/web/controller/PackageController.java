package com.web.controller;

import com.web.model.response.PackageResponse;
import com.web.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PackageController {

    private PackageService packageServiceImpl;

    @Autowired
    public void setPackageServiceImpl(PackageService packageServiceImpl) {
        this.packageServiceImpl = packageServiceImpl;
    }

    public List<PackageResponse> getAllPackage(){
        return packageServiceImpl.getAllPackages();
    }


}
