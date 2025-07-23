package com.web.service.impl;

import com.web.converter.PackageConverter;
import com.web.entity.PackageEntity;
import com.web.enums.operation.ComparisonOperator;
import com.web.exception.ErrorResponse;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.PackageDTO;
import com.web.model.response.PackageResponse;
import com.web.repository.MemberPackageRepository;
import com.web.repository.PackageRepository;
import com.web.service.PackageService;
import com.web.utils.FieldNameUtils;
import com.web.utils.MergeObjectUtils;
import com.web.utils.PageUtils;
import com.web.utils.specification.SpecificationUtils;
import com.web.utils.specification.search.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PackageServiceImpl implements PackageService {
    private PackageRepository packageRepository;
    private MemberPackageRepository memberPackageRepository;
    private PackageConverter packageConverter;
    private SpecificationUtils<PackageEntity> specificationUtils;
    private PageUtils<PackageEntity> pageUtils;
    private MergeObjectUtils mergeObjectUtils;

    @Autowired
    public void setSpecificationUtils(SpecificationUtils<PackageEntity> specificationUtils) {
        this.specificationUtils = specificationUtils;
    }

    @Autowired
    public void setMemberPackageRepository(MemberPackageRepository memberPackageRepository) {
        this.memberPackageRepository = memberPackageRepository;
    }

    @Autowired
    public void setPackageRepository(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Autowired
    public void setPackageConverter(PackageConverter packageConverter) {
        this.packageConverter = packageConverter;
    }

    @Autowired
    public void setPageUtils(PageUtils<PackageEntity> pageUtils) {
        this.pageUtils = pageUtils;
    }

    @Autowired
    public void setMergeObjectUtils(MergeObjectUtils mergeObjectUtils) {
        this.mergeObjectUtils = mergeObjectUtils;
    }

    @Override
    public List<PackageResponse> getAllPackages() {
        List<PackageEntity> packageEntities = packageRepository.findAll();
        return packageEntities.stream().map(packageConverter::toResponse).toList();
    }

    @Override
    public PackageResponse getPackage(Long id) {
        return packageConverter.toResponse(packageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PackageEntity.class, id)));
    }

    @Override
    public PackageEntity getPackageById(Long id) {
        return null;
    }

    @Override
    public void savePackage(PackageDTO packageDTO) {
        if (packageRepository.existsByPackageCode(packageDTO.getPackageCode())) {
            throw new ErrorResponse("Mã gói tập đã tồn tại", 400);
        }
        packageRepository.save(packageConverter.toEntity(packageDTO));
    }

    @Override
    public void updatePackage(Long id, PackageDTO packageDTO) {
        packageDTO.setPackageCode(null); // Prevent updating package code
        PackageEntity target = packageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PackageEntity.class, id));
        PackageEntity source = packageConverter.toEntity(packageDTO);
        mergeObjectUtils.mergeNonNullFields(source, target);
        packageRepository.save(target);
    }

    @Override
    public void deletePackage(Long id) {
        // Check if the package exists
        PackageEntity packageEntity = packageRepository.findById(id).orElseThrow(() -> new ErrorResponse("danger", "Gói tập không tồn tại"));
        // Check if the package is subscribed by any members
        if (memberPackageRepository.existsByPackageEntityId(id))
            throw new ErrorResponse("danger", "Gói tập đang được sử dụng bởi thành viên, không thể xóa");
        packageRepository.delete(packageEntity);
    }

    @Override
    public Page<PackageResponse> getAllPackages(int page, int size, PackageDTO packageDTO) {
        Sort sort = Sort.unsorted();
        if (packageDTO.getSortFieldName() != null && packageDTO.getSortDirection() != null) {
            sort = Sort.by(Sort.Direction.fromString(packageDTO.getSortDirection()), packageDTO.getSortFieldName());
        }
        Pageable pageable = pageUtils.getPageable(page, size, sort);
        List<SearchCriteria> searchCriterias = List.of(new SearchCriteria(FieldNameUtils.joinFields(PackageEntity.Fields.name), ComparisonOperator.CONTAINS, packageDTO.getName(), null), new SearchCriteria(FieldNameUtils.joinFields(PackageEntity.Fields.status), ComparisonOperator.EQUALS, packageDTO.getStatus(), null));
        return packageRepository.findAll(specificationUtils.reset().getSearchSpecifications(searchCriterias), pageable).map(packageConverter::toResponse);
    }

    @Override
    public Long countPackages() {
        return packageRepository.count();
    }

    @Override
    public Long countInactivePackages() {
        return packageRepository.countByStatus("inactive");
    }

    @Override
    public Long countActivePackages() {
        return packageRepository.countByStatus("active");
    }
}
