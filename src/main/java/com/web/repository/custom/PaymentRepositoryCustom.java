package com.web.repository.custom;

import java.util.List;

public interface PaymentRepositoryCustom {
    Long getTotalRevenueMonthly(int month, int year);

    List<Long> getTotalRevenueOfRecentMonths(int month, int year, int months);
}
