package com.web.repository.custom.impl;

import com.web.repository.custom.MemberPackageRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MemberPackageRepositoryCustomImpl implements MemberPackageRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public MemberPackageRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Map<String, Long> getPackageCountByName() {
        String jpql = """
                SELECT mp.packageEntity.name, COUNT(mp.id)
                FROM MemberPackageEntity mp
                WHERE mp.isActive = true AND mp.endDate > CURRENT_DATE AND mp.startDate <= CURRENT_DATE
                GROUP BY mp.packageEntity.name
                """;
        List<Object[]> results = entityManager.createQuery(jpql).getResultList();
        Map<String, Long> packageCountMap = new java.util.LinkedHashMap<>();
        for (Object[] result : results) {
            String packageType = (String) result[0];
            Long count = (Long) result[1];
            packageCountMap.put(packageType, count);
        }
        return packageCountMap;
    }
}
