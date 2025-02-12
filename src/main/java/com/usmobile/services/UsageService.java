package com.usmobile.services;

import com.usmobile.models.Cycle;
import com.usmobile.models.DailyUsage;
import com.usmobile.repositories.CycleRepository;
import com.usmobile.repositories.DailyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class UsageService {

    @Autowired
    private DailyUsageRepository dailyUsageRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Transactional
    public void transferMdn(String mdn, String fromUserId, String toUserId) {
        var cycles = cycleRepository.findByUserIdAndMdn(fromUserId, mdn);
        cycles.forEach(cycle -> {
            Cycle updatedCycle = new Cycle();
            updatedCycle.setId(cycle.getId());
            updatedCycle.setMdn(cycle.getMdn());
            updatedCycle.setUserId(toUserId);
            updatedCycle.setStartDate(cycle.getStartDate());
            updatedCycle.setEndDate(cycle.getEndDate());
            cycleRepository.save(updatedCycle);
        });

        List<DailyUsage> usages = dailyUsageRepository.findByUserIdAndMdn(fromUserId, mdn);
        usages.forEach(usage -> {
            DailyUsage updatedUsage = new DailyUsage();
            updatedUsage.setId(usage.getId());
            updatedUsage.setMdn(usage.getMdn());
            updatedUsage.setUserId(toUserId);
            updatedUsage.setUsageDate(usage.getUsageDate());
            updatedUsage.setUsedInMb(usage.getUsedInMb());
            
            dailyUsageRepository.save(updatedUsage);
        });
    }

    public List<DailyUsage> getCurrentCycleUsage(String userId, String mdn) {

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }

        Cycle currentCycle = cycleRepository.findByUserIdAndMdn(userId, mdn)
            .stream()
            .filter(cycle -> cycle.getEndDate().after(new Date()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No active cycle found"));

        return dailyUsageRepository
                .findByUserIdAndMdnAndUsageDateBetween(userId, mdn, currentCycle.getStartDate(), currentCycle.getEndDate());
    }

    public List<Cycle> getCycleHistory(String userId, String mdn) {
        return cycleRepository.findByUserIdAndMdn(userId, mdn);
    }

    public DailyUsage updateUsage(String mdn, Date usageDate, double usedInMb) {
        var usage = dailyUsageRepository.findByMdnAndUsageDate(mdn, usageDate)
                .orElseThrow(() -> new RuntimeException("No usage found for the given date"));
        
        var updatedUsage = new DailyUsage();
        updatedUsage.setId(usage.getId());
        updatedUsage.setMdn(usage.getMdn());
        updatedUsage.setUserId(usage.getUserId());
        updatedUsage.setUsageDate(usage.getUsageDate());
        updatedUsage.setUsedInMb(usedInMb);        

        return dailyUsageRepository.save(updatedUsage);
    }
}
