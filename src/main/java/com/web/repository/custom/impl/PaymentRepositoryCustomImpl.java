package com.web.repository.custom.impl;

import com.web.repository.custom.PaymentRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long getTotalRevenueMonthly(int month, int year) {
        String jpql = """
                SELECT COALESCE(SUM(p.amount), 0)
                FROM PaymentEntity p
                WHERE FUNCTION('DATE_FORMAT', p.paymentDate, '%m') = :month
                AND FUNCTION('DATE_FORMAT', p.paymentDate, '%Y') = :year
                AND p.status = 'COMPLETED'
                """;
        TypedQuery<Double> typedQuery = entityManager.createQuery(jpql, Double.class);
        typedQuery.setParameter("month", month);
        typedQuery.setParameter("year", year);
        return typedQuery.getSingleResult().longValue();
    }

    @Override
    public List<Long> getTotalRevenueOfRecentMonths(int month, int year, int months) {
        String jpql = """
                SELECT COALESCE(SUM(p.amount), 0)
                FROM PaymentEntity p
                WHERE p.paymentDate <= :date
                AND p.status = 'COMPLETED'
                ORDER BY FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m')
                """;
        TypedQuery<Long> typedQuery = entityManager.createQuery(jpql, Long.class);
        typedQuery.setParameter("date", java.sql.Date.valueOf(year + "-" + String.format("%02d", month) + "-01"));
        typedQuery.setMaxResults(months);
        return typedQuery.getResultList();
    }
}
