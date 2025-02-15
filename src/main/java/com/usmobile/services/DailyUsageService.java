package com.usmobile.services;

import com.usmobile.models.DailyUsage;
import com.usmobile.repositories.DailyUsageCriteria;
import com.usmobile.repositories.CycleCriteria;
import com.usmobile.models.Cycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class DailyUsageService {


    private final MongoTemplate mongoTemplate;

    @Autowired
    public DailyUsageService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private List<DailyUsage> executeQuery(Criteria criteria)
    {
        Query query = new Query(criteria);
        return mongoTemplate.find(query, DailyUsage.class);
    }

    private Criteria and(Criteria... criteria) {
        return new Criteria().andOperator(criteria);
    }

    public List<DailyUsage> findDailyUsage(List<Criteria> filters) {
        Criteria combinedCriteria = and(
            filters.toArray(new Criteria[0])
        );
        return executeQuery(combinedCriteria);
    }

    @Transactional
    public void transferMdn(String mdn, String fromUserId, String toUserId) {
        Criteria cycleCriteria = 
            CycleCriteria
            .filterByMdn(mdn)
            .andOperator(CycleCriteria.filterByUserId(fromUserId));
        Query cycleQuery = new Query(cycleCriteria);
        List<Cycle> cycles = mongoTemplate.find(cycleQuery, Cycle.class);

        cycles.forEach(cycle -> {
            Cycle updatedCycle = new Cycle();
            updatedCycle.setId(cycle.getId());
            updatedCycle.setMdn(cycle.getMdn());
            updatedCycle.setUserId(toUserId);
            updatedCycle.setStartDate(cycle.getStartDate());
            updatedCycle.setEndDate(cycle.getEndDate());
            mongoTemplate.save(updatedCycle);
        });

        Criteria usageCriteria =
            DailyUsageCriteria
            .filterByMdn(mdn)
            .andOperator(DailyUsageCriteria.filterByUserId(fromUserId));
        Query usageQuery = new Query(usageCriteria);
        List<DailyUsage> usages = mongoTemplate.find(usageQuery, DailyUsage.class);
        
        usages.forEach(usage -> {
            DailyUsage updatedUsage = new DailyUsage();
            updatedUsage.setId(usage.getId());
            updatedUsage.setMdn(usage.getMdn());
            updatedUsage.setUserId(toUserId);
            updatedUsage.setUsageDate(usage.getUsageDate());
            updatedUsage.setUsedInMb(usage.getUsedInMb());
            
            mongoTemplate.save(updatedUsage);
        });
    }

    @Transactional
    public DailyUsage updateUsage(String mdn, Date usageDate, double usedInMb) {
        Criteria criteria = 
            DailyUsageCriteria
            .filterByMdn(mdn)
            .andOperator(DailyUsageCriteria.filterByUsageDateWithin12Hours(usageDate));
        Query query = new Query(criteria);
        DailyUsage usage = mongoTemplate.findOne(query, DailyUsage.class);

        if (usage == null) {
            throw new RuntimeException("No usage found for the given date");
        }

        DailyUsage updatedUsage = new DailyUsage();
        updatedUsage.setId(usage.getId());
        updatedUsage.setMdn(usage.getMdn());
        updatedUsage.setUserId(usage.getUserId());
        updatedUsage.setUsageDate(usage.getUsageDate());
        updatedUsage.setUsedInMb(usedInMb);
        mongoTemplate.save(updatedUsage);

        return updatedUsage;
    }
}
