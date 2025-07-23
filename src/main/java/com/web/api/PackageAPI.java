package com.web.api;

import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;
import com.web.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
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

    @GetMapping("/api/admin/package/page/{pageIndex}")
    public Map<String, Object> getAllPackagesForAdmin(@PathVariable int pageIndex, @ModelAttribute PackageDTO packageDTO) {
        Page<PackageResponse> packageResponsePage = packageService.getAllPackages(pageIndex, 6, packageDTO);
        return Map.of("items", packageResponsePage.getContent(), "currentPage", pageIndex, "totalPages", packageResponsePage.getTotalPages());
    }

    @PostMapping("/api/admin/package")
    public void savePackage(@RequestBody @Valid PackageDTO packageDTO) {
        packageService.savePackage(packageDTO);
    }

    @PutMapping("/api/admin/package/{packageId}")
    public void updatePackage(@PathVariable Long packageId, @RequestBody @Valid PackageDTO packageDTO) {
        packageService.updatePackage(packageId, packageDTO);
    }
}
