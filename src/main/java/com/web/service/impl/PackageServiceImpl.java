package com.web.service.impl;

import com.web.converter.PackageConverter;
import com.web.entity.PackageEntity;
import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;
import com.web.repository.PackageRepository;
import com.web.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class PackageServiceImpl implements PackageService {
    @Autowired
    private PackageRepository packageRepositoryImpl;

    @Autowired
    private PackageConverter packageConverter;

    @Override
    public List<PackageResponse> getAllPackages() {

        List<PackageEntity> packageEntities = packageRepositoryImpl.findAll();

        return packageEntities.stream().map(packageConverter::toConverterPackage).toList();
    }

    @Override
    public PackageEntity getPackageById(Long id) {
        return null;
    }

    @Override
    public void savePackage(PackageDTO packageDTO) {
        packageRepositoryImpl.save(packageConverter.toConverterPackageEntity(packageDTO));
    }

    @Override
    public void updatePackage(PackageDTO packageDTO) {
        packageRepositoryImpl.save(packageConverter.toConverterPackageEntity(packageDTO));
    }

    @Override
    public void deletePackage(Long id) {
        packageRepositoryImpl.deleteById(id);
    }
}
