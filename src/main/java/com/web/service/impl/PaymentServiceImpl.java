package com.web.service.impl;

import com.web.repository.custom.PaymentRepositoryCustom;
import com.web.service.PaymentService;
import com.web.utils.TimestampUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private PaymentRepositoryCustom paymentRepositoryCustom;
    private TimestampUtils timestampUtils;

    @Autowired
    public void setPaymentRepositoryCustom(PaymentRepositoryCustom paymentRepositoryCustom) {
        this.paymentRepositoryCustom = paymentRepositoryCustom;
    }

    @Autowired
    public void setTimestampUtils(TimestampUtils timestampUtils) {
        this.timestampUtils = timestampUtils;
    }

    @Override
    public Long getRevenueThisMonth() {
        int month = timestampUtils.getMonth();
        int year = timestampUtils.getYear();

        // Call the custom repository method to get the total payment for this month
        return paymentRepositoryCustom.getTotalRevenueMonthly(month, year);
    }

    @Override
    public Map<String, Long> getRevenues6MonthsAgo() {
        Map<String, Long> revenueMap = new LinkedHashMap<>();
        timestampUtils.setTime();
        for (int i = 5; i >= 0; i--) {
            int month = timestampUtils.minusMonths(i).toLocalDateTime().getMonthValue();
            int year = timestampUtils.minusMonths(i).toLocalDateTime().getYear();
            Long revenue = paymentRepositoryCustom.getTotalRevenueMonthly(month, year) / 1_000_000;
            revenueMap.put(month + "/" + year, revenue);
        }
        return revenueMap;
    }
}
