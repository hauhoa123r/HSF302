package com.web.converter;

import com.web.entity.EquipmentEntity;
import com.web.entity.PromotionEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.EquipmentDTO;
import com.web.model.dto.PromotionsDTO;
import com.web.model.response.PromotionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

import static org.thymeleaf.util.NumberUtils.formatCurrency;

@Component
public class PromotionConverter {
    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PromotionResponse toResponse(PromotionEntity promotionEntity) {
        return Optional.ofNullable(modelMapper.map(promotionEntity, PromotionResponse.class))
                .orElseThrow(() -> new ErrorMappingException(PromotionEntity.class, PromotionResponse.class));
    }

    public PromotionsDTO toDTO(PromotionEntity promotionEntity) {
        PromotionsDTO promotionsDTO = modelMapper.map(promotionEntity, PromotionsDTO.class);
        if ("PERCENT".equalsIgnoreCase(promotionEntity.getDiscountType())) {
            promotionsDTO.setDiscountDisplay(promotionEntity.getDiscountPercent() + "%");
        } else if ("FIXED".equalsIgnoreCase(promotionEntity.getDiscountType())) {
            promotionsDTO.setDiscountDisplay(formatCurrency(promotionEntity.getMaximumDiscount()));
        } else {
            promotionsDTO.setDiscountDisplay("Không xác định");
        }
        return promotionsDTO;
    }
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0₫";
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return nf.format(amount);
    }
}
