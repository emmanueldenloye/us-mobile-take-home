package com.usmobile.services;

import com.usmobile.repositories.DailyUsageRepository;
import com.usmobile.models.DailyUsage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Service
public class UsageUpdateScheduler {
    private final DailyUsageRepository dailyUsageRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsageUpdateScheduler.class);

    public UsageUpdateScheduler(DailyUsageRepository dailyUsageRepository) {
        this.dailyUsageRepository = dailyUsageRepository;
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void updateUsageData() {
        

        logger.info("Updating usage data");
        var now = new Date();

        dailyUsageRepository.findAll().forEach(usage -> {
            if (usage.getUsageDate().equals(now)) {
                double newUsage = calculateNewUsage(usage.getUsedInMb());
                var updatedUsage = new DailyUsage();
                updatedUsage.setId(usage.getId());
                updatedUsage.setMdn(usage.getMdn());
                updatedUsage.setUserId(usage.getUserId());
                updatedUsage.setUsageDate(usage.getUsageDate());
                updatedUsage.setUsedInMb(newUsage);

                dailyUsageRepository.save(usage);
            }
        });
    }

    private double calculateNewUsage(double currentUsage) {
        return currentUsage + 100.0;
    }
}
