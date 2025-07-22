package com.web.repository;

import com.web.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    boolean existsByIdAndStatus(Long id, String status);
}
