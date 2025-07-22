package com.web.service;

import java.util.Map;

public interface PaymentService {
    Long getRevenueThisMonth();

    Map<String, Long> getRevenues6MonthsAgo();
}
