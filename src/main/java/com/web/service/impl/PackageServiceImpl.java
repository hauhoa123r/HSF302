package com.web.service.impl;

import com.web.converter.PackageConverter;
import com.web.entity.PackageEntity;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;
import com.web.repository.PackageRepository;
import com.web.service.PackageService;
import com.web.utils.specification.SpecificationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PackageServiceImpl implements PackageService {
    private PackageRepository packageRepository;
    private PackageConverter packageConverter;
    private SpecificationUtils<PackageEntity> specificationUtils;

    @Autowired
    public void setSpecificationUtils(SpecificationUtils<PackageEntity> specificationUtils) {
        this.specificationUtils = specificationUtils;
    }

    @Autowired
    public void setPackageRepository(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Autowired
    public void setPackageConverter(PackageConverter packageConverter) {
        this.packageConverter = packageConverter;
    }

    @Override
    public List<PackageResponse> getAllPackages() {
        List<PackageEntity> packageEntities = packageRepository.findAll();
        return packageEntities.stream().map(packageConverter::toResponse).toList();
    }

    @Override
    public PackageResponse getPackage(Long id) {
        return packageConverter.toResponse(packageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PackageEntity.class, id)));
    }

    @Override
    public PackageEntity getPackageById(Long id) {
        return null;
    }

    @Override
    public void savePackage(PackageDTO packageDTO) {
        packageRepository.save(packageConverter.toConverterPackageEntity(packageDTO));
    }

    @Override
    public void updatePackage(PackageDTO packageDTO) {
        packageRepository.save(packageConverter.toConverterPackageEntity(packageDTO));
    }

    @Override
    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }

    @Override
    public Page<PackageResponse> getAllPackages(int page, int size, PackageDTO packageDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPackages'");
    }
}
