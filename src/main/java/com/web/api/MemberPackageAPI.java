package com.web.api;

import com.web.model.dto.MemberPackageDTO;
import com.web.service.MemberPackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberPackageAPI {
    private MemberPackageService memberPackageService;

    @Autowired
    public void setMemberPackageService(MemberPackageService memberPackageService) {
        this.memberPackageService = memberPackageService;
    }

    @PostMapping("/api/admin/member/package")
    public void createMemberPackage(@RequestBody @Valid MemberPackageDTO memberPackageDTO) {
        memberPackageService.createMemberPackage(memberPackageDTO);
    }
}
