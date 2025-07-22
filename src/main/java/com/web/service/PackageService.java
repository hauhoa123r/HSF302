package com.web.service;

import com.web.entity.PackageEntity;
import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;

import java.util.List;

import org.springframework.data.domain.Page;

public interface PackageService {
    List<PackageResponse> getAllPackages();

    PackageResponse getPackage(Long id);

    PackageEntity getPackageById(Long id);

    void savePackage(PackageDTO packageDTO);

    void updatePackage(PackageDTO packageDTO);

    void deletePackage(Long id);

    Page<PackageResponse> getAllPackages(int page, int size, PackageDTO packageDTO);
}
