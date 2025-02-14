package com.usmobile.services;

import com.usmobile.models.DailyUsage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
public class UsageUpdateScheduler {

    private final MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UsageUpdateScheduler.class);
    private final DailyUsageService dailyUsageService;

    @Autowired
    public UsageUpdateScheduler(MongoTemplate mongoTemplate, DailyUsageService dailyUsageService) {
        this.mongoTemplate = mongoTemplate;
        this.dailyUsageService = dailyUsageService;
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void updateUsageData() {
        

        logger.info("Updating usage data");
        var now = new Date();

        List<Criteria> filters = List.of();

        dailyUsageService.findDailyUsage(filters).forEach(usage -> {
            if (usage.getUsageDate().equals(now)) {
                double newUsage = calculateNewUsage(usage.getUsedInMb());
                var updatedUsage = new DailyUsage();
                updatedUsage.setId(usage.getId());
                updatedUsage.setMdn(usage.getMdn());
                updatedUsage.setUserId(usage.getUserId());
                updatedUsage.setUsageDate(usage.getUsageDate());
                updatedUsage.setUsedInMb(newUsage);

                mongoTemplate.save(usage);
            }
        });
    }

    private double calculateNewUsage(double currentUsage) {
        return currentUsage + 100.0; //arbirtary value
    }
}
