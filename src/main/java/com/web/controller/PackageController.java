package com.web.controller;

import com.web.exception.ErrorResponse;
import com.web.model.response.PackageResponse;
import com.web.service.MemberPackageService;
import com.web.service.PackageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PackageController {

    private PackageService packageService;
    private MemberPackageService memberPackageService;

    @Autowired
    public void setPackageService(PackageService packageService) {
        this.packageService = packageService;
    }

    @Autowired
    public void setMemberPackageService(MemberPackageService memberPackageService) {
        this.memberPackageService = memberPackageService;
    }

    public List<PackageResponse> getAllPackage() {
        return packageService.getAllPackages();
    }

    @GetMapping("/admin/package")
    public String showPackagePageForAdmin(ModelMap modelMap, HttpServletRequest httpServletRequest) {
        modelMap.put("packagesCount", packageService.countPackages());
        modelMap.put("activePackagesCount", packageService.countActivePackages());
        modelMap.put("inactivePackagesCount", packageService.countInactivePackages());
        modelMap.put("activeMemberPackagesCount", memberPackageService.countActiveMemberPackages());
        return "admin/packages/list";
    }

    @GetMapping("/admin/package/add")
    public String showPackageAddPageForAdmin(ModelMap modelMap) {
        modelMap.put("package", new PackageResponse());
        return "admin/packages/add";
    }

    @GetMapping("/admin/package/detail/{id}")
    public String showPackageDetailPageForAdmin(ModelMap modelMap, @PathVariable Long id) {
        PackageResponse packageResponse = packageService.getPackage(id);
        modelMap.put("package", packageResponse);
        return "admin/packages/detail";
    }

    @GetMapping("/admin/package/edit/{id}")
    public String showPackageEditPageForAdmin(ModelMap modelMap, @PathVariable Long id) {
        PackageResponse packageResponse = packageService.getPackage(id);
        modelMap.put("package", packageResponse);
        return "admin/packages/edit";
    }

    @GetMapping("/admin/package/delete/{id}")
    public String showPackageDeletePageForAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            packageService.deletePackage(id);
            redirectAttributes.addFlashAttribute("toastType", "success");
            redirectAttributes.addFlashAttribute("toastMessage", "Xóa gói thành công");
        } catch (ErrorResponse ex) {
            redirectAttributes.addFlashAttribute("toastType", ex.getToastType());
            redirectAttributes.addFlashAttribute("toastMessage", ex.getMessage());
        }
        return "redirect:/admin/package";
    }
}
