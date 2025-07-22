package com.web.repository;

import com.web.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>, JpaSpecificationExecutor<MemberEntity> {
    List<MemberEntity> findByUserEntityFullNameContaining(String name);

    boolean existsByUserEntityEmail(String userEntityEmail);

    boolean existsByUserEntityPhone(String userEntityPhone);

    boolean existsByUserEntityIdCard(String userEntityIdCard);
}
