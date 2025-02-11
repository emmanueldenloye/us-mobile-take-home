package com.usmobile.services;

import com.usmobile.models.Cycle;
import com.usmobile.models.DailyUsage;
import com.usmobile.repositories.CycleRepository;
import com.usmobile.repositories.DailyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class UsageService {

    @Autowired
    private DailyUsageRepository dailyUsageRepository;

    @Autowired
    private CycleRepository cycleRepository;

    public List<DailyUsage> getCurrentCycleUsage(String userId, String mdn) {
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
}
