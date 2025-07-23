package com.web.service;

import com.web.model.dto.MemberPackageDTO;

import java.util.Map;

public interface MemberPackageService {
    Map<String, Long> getPackageCountByName();

    void cancelMemberPackage(Long memberId);

    void createMemberPackage(MemberPackageDTO memberPackageDTO);

    Long countActiveMemberPackages();
}
