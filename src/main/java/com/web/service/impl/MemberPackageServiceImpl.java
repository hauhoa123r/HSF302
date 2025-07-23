package com.web.service.impl;

import com.web.converter.MemberPackageConverter;
import com.web.entity.MemberPackageEntity;
import com.web.entity.PackageEntity;
import com.web.entity.PaymentEntity;
import com.web.entity.PromotionEntity;
import com.web.exception.ErrorResponse;
import com.web.model.dto.MemberPackageDTO;
import com.web.repository.MemberPackageRepository;
import com.web.repository.PackageRepository;
import com.web.repository.PaymentRepository;
import com.web.repository.PromotionRepository;
import com.web.repository.custom.MemberPackageRepositoryCustom;
import com.web.service.MemberPackageService;
import com.web.utils.TimestampUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

@Service
@Transactional
public class MemberPackageServiceImpl implements MemberPackageService {

    private MemberPackageRepositoryCustom memberPackageRepositoryCustom;
    private MemberPackageRepository memberPackageRepository;
    private PackageRepository packageRepository;
    private PromotionRepository promotionRepository;
    private PaymentRepository paymentRepository;
    private MemberPackageConverter memberPackageConverter;
    private TimestampUtils timestampUtils;

    @Autowired
    public void setMemberPackageRepositoryCustom(MemberPackageRepositoryCustom memberPackageRepositoryCustom) {
        this.memberPackageRepositoryCustom = memberPackageRepositoryCustom;
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
    public void setPromotionRepository(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Autowired
    public void setMemberPackageConverter(MemberPackageConverter memberPackageConverter) {
        this.memberPackageConverter = memberPackageConverter;
    }

    @Autowired
    public void setTimestampUtils(TimestampUtils timestampUtils) {
        this.timestampUtils = timestampUtils;
    }

    @Override
    public Map<String, Long> getPackageCountByName() {
        return memberPackageRepositoryCustom.getPackageCountByName();
    }

    @Override
    public void cancelMemberPackage(Long memberId) {
        memberPackageRepository.cancelMemberPackage(memberId);
    }

    @Override
    public void createMemberPackage(MemberPackageDTO memberPackageDTO) {
        // Kiem tra xem ngay bat dau co hop le hay khong
        if (memberPackageDTO.getStartDate() == null) {
            throw new ErrorResponse("Ngày bắt đầu không được để trống", 400);
        }

        Date currentDate = timestampUtils.setTime().getStartOfDayAsDate();
        if (memberPackageDTO.getStartDate().before(currentDate)) {
            throw new ErrorResponse("Ngày bắt đầu không được trước ngày hiện tại", 400);
        }

        // Kiem tra goi tap co ton tai hay khong
        PackageEntity packageEntity = packageRepository.findById(memberPackageDTO.getPackageEntityId())
                .orElseThrow(() -> new ErrorResponse("Gói tập không tồn tại", 404));

        // Kiem tra goi tap con hoat dong hay khong
        if (!packageEntity.getStatus().equals("ACTIVE")) {
            throw new ErrorResponse("Gói tập không còn hoạt động", 400);
        }

        // Kiem tra khuyen mai co ton tai hay khong
        PromotionEntity promotionEntity = promotionRepository.findById(memberPackageDTO.getPromotionEntityId())
                .orElseThrow(() -> new ErrorResponse("Khuyến mãi không tồn tại", 404));

        // Kiem tra khuyen mai con hoat dong hay khong
        if (!promotionEntity.getStatus().equals("ACTIVE")) {
            throw new ErrorResponse("Khuyến mãi không còn hoạt động", 400);
        }

        // Kiem tra khuyen mai con du so luong su dung hay khong
        Long promotionUsageCount = memberPackageRepository.countByPromotionEntityId(memberPackageDTO.getPromotionEntityId());
        Long promotionMaxUsage = promotionEntity.getMaxUsage();
        if (promotionMaxUsage != null && promotionUsageCount >= promotionMaxUsage) {
            throw new ErrorResponse("Khuyến mãi đã hết lượt sử dụng", 400);
        }

        // Xoa goi cu neu co
        memberPackageRepository.cancelMemberPackage(memberPackageDTO.getMemberEntityId());

        // Tao goi tap moi
        // Set end date tu dong
        MemberPackageEntity memberPackageEntity = memberPackageConverter.toEntity(memberPackageDTO);
        memberPackageEntity.setEndDate(timestampUtils.setTime(memberPackageEntity.getStartDate()).plusDaysAsDate(packageEntity.getDurationDays().intValue()));
        Float packageEntityPrice = packageEntity.getPrice();
        Float promotionEntityDiscountPercent = promotionEntity.getDiscountPercent();
        memberPackageEntity.setFinalPrice(new BigDecimal(packageEntityPrice * (1 - promotionEntityDiscountPercent / 100)));
        memberPackageEntity.setIsActive(false);
        memberPackageEntity = memberPackageRepository.save(memberPackageEntity);

        // Tao giao dich thanh toan voi status PENDING
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setMemberPackage(memberPackageEntity);
        paymentEntity.setAmount(memberPackageEntity.getFinalPrice().floatValue());
        paymentEntity.setMethod(memberPackageDTO.getMethod());
        paymentEntity.setStatus("PENDING");
        paymentRepository.save(paymentEntity);
    }

    @Override
    public Long countActiveMemberPackages() {
        return memberPackageRepository.countByIsActive(true);
    }
}
