package com.web.api;

import com.web.model.response.PackagesReponse;
import com.web.service.PackagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/packages")
public class PackagesAPI {
    @Autowired
    private PackagesService packagesService;

    @GetMapping
    public PackagesReponse getPackages(@PathVariable Long id) {
        PackagesReponse packagesReponse = packagesService.getPackage(id);
        return packagesReponse;
    }

    @DeleteMapping
    public String deletePackage(@PathVariable Long id) {
        packagesService.deletePackage(id);
        return "Deleted";
    }
}
