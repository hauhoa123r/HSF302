package com.web.repository;

import com.web.entity.MemberPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberPackageRepository extends JpaRepository<MemberPackageEntity, Long> {

    @Modifying
    @Query("UPDATE MemberPackageEntity m SET m.isActive = false WHERE m.memberEntity.id = ?1")
    void cancelMemberPackage(Long memberId);

    Long countByPromotionEntityId(Long promotionEntityId);

    int countByMemberEntity_Id(Long memberEntityId);

    Long countByIsActive(boolean isActive);

    boolean existsByPackageEntityId(Long packageEntityId);
}
