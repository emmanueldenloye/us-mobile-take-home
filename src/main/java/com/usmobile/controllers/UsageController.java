package com.usmobile.controllers;

import com.usmobile.models.Cycle;
import com.usmobile.models.DailyUsage;
import com.usmobile.services.CycleService;
import com.usmobile.services.DailyUsageService;
import com.usmobile.repositories.DailyUsageCriteria;
import com.usmobile.repositories.CycleCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;
import java.util.Arrays;

@RestController
@RequestMapping("/usage")
public class UsageController {

    private final DailyUsageService dailyUsageService;
    private final CycleService cycleService;

    @Autowired
    public UsageController(DailyUsageService dailyUsageService, CycleService cycleService) {
        this.dailyUsageService = dailyUsageService;
        this.cycleService = cycleService;
    }

    @GetMapping("/current")
    public List<DailyUsage> getCurrentCycleUsage(@RequestParam String userId, @RequestParam String mdn) {
        System.out.println("Found cycles: " + cycleService.findCycles(Arrays.asList(
            CycleCriteria.filterByUserId(userId),
            CycleCriteria.filterByMdn(mdn))
        )
        );
        Cycle currentCycle = cycleService.findCycles(Arrays.asList(
            CycleCriteria.filterByUserId(userId),
            CycleCriteria.filterByMdn(mdn)))
            .stream()
            .filter(cycle -> cycle.getEndDate().after(new Date()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No active cycle found"));
        return dailyUsageService.findDailyUsage(Arrays.asList(
            DailyUsageCriteria.filterByUserId(userId),
            DailyUsageCriteria.filterByMdn(mdn),
            DailyUsageCriteria.filterByUsageDateBetween(currentCycle.getStartDate(), currentCycle.getEndDate()))
        );
    }
    
    @GetMapping("/daily")
    public List<DailyUsage> getDailyUsage(
        @RequestParam String mdn,
        @RequestParam Date usageDate) {
        return dailyUsageService.findDailyUsage(Arrays.asList(
            DailyUsageCriteria.filterByMdn(mdn),
            DailyUsageCriteria.filterByUsageDate(usageDate)
        )
        );
    }
        

    @GetMapping("/history")
    public List<Cycle> getCycleHistory(@RequestParam String userId, @RequestParam String mdn) {
        return cycleService.findCycles(List.of(
            CycleCriteria.filterByUserId(userId),
            CycleCriteria.filterByMdn(mdn)
        )
        );
    }

    @PostMapping("/transfer")
    public void transferMdn(@RequestParam String mdn, @RequestParam String fromUserId, @RequestParam String toUserId) {
        dailyUsageService.transferMdn(mdn, fromUserId, toUserId);
    }

    @PostMapping("/update")
    public DailyUsage updateUsage(
        @RequestParam String mdn,
        @RequestParam Date usageDate,
        @RequestParam double usedInMb) {
        return dailyUsageService.updateUsage(mdn, usageDate, usedInMb);
    }
}