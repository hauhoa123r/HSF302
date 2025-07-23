package com.web.service;

import com.web.entity.PackageEntity;
import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PackageService {
    List<PackageResponse> getAllPackages();

    PackageResponse getPackage(Long id);

    PackageEntity getPackageById(Long id);

    void savePackage(PackageDTO packageDTO);

    void updatePackage(Long id, PackageDTO packageDTO);

    void deletePackage(Long id);

    Page<PackageResponse> getAllPackages(int page, int size, PackageDTO packageDTO);

    Long countPackages();

    Long countActivePackages();

    Long countInactivePackages();
}
