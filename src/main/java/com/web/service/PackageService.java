package com.web.service;

import com.web.entity.PackageEntity;
import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;

import java.util.List;

public interface PackageService {
    List<PackageResponse> getAllPackages();
    PackageEntity getPackageById(Long id);
    void savePackage(PackageDTO packageDTO);
    void updatePackage(PackageDTO packageDTO);
    void deletePackage(Long id);

}
