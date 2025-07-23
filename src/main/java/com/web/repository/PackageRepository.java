package com.web.repository;

import com.web.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PackageRepository extends JpaRepository<PackageEntity, Long>, JpaSpecificationExecutor<PackageEntity> {
    boolean existsByIdAndStatus(Long id, String status);

    Long countByStatus(String status);

    boolean existsByPackageCode(String packageCode);
}
