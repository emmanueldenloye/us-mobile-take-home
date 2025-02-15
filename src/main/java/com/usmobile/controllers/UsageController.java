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

    /**
     * Retrieves the current cycle usage for a given user and MDN (Mobile Directory Number).
     *
     * @param userId the ID of the user whose usage is being queried
     * @param mdn the Mobile Directory Number associated with the user
     * @return a list of DailyUsage objects representing the usage data for the current cycle
     * @throws RuntimeException if no active cycle is found for the given user and MDN
     */
    @GetMapping("/current")
    public List<DailyUsage> getCurrentCycleUsage(@RequestParam String userId, @RequestParam String mdn) {
        System.out.println("Found cycles: " + cycleService.findCycles(List.of(
            CycleCriteria.filterByUserId(userId),
            CycleCriteria.filterByMdn(mdn))
        )
        );
        Cycle currentCycle = cycleService.findCycles(List.of(
            CycleCriteria.filterByUserId(userId),
            CycleCriteria.filterByMdn(mdn)))
            .stream()
            .filter(cycle -> cycle.getEndDate().after(new Date()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No active cycle found"));
        return dailyUsageService.findDailyUsage(List.of(
            DailyUsageCriteria.filterByUserId(userId),
            DailyUsageCriteria.filterByMdn(mdn),
            DailyUsageCriteria.filterByUsageDateBetween(currentCycle.getStartDate(), currentCycle.getEndDate()))
        );
    }
    
    /**
     * Retrieves the daily usage data for a given mobile directory number (MDN) and usage date.
     *
     * @param mdn the mobile directory number to filter the usage data
     * @param usageDate the date to filter the usage data
     * @return a list of DailyUsage objects that match the specified MDN and usage date
     */
    @GetMapping("/daily")
    public List<DailyUsage> getDailyUsage(
        @RequestParam String mdn,
        @RequestParam Date usageDate) {
        return dailyUsageService.findDailyUsage(List.of(
            DailyUsageCriteria.filterByMdn(mdn),
            DailyUsageCriteria.filterByUsageDateWithin12Hours(usageDate)
        )
        );
    }

    /**
     * Retrieves the cycle history for a given user and MDN (Mobile Directory Number).
     *
     * @param userId the ID of the user whose cycle history is to be retrieved
     * @param mdn the Mobile Directory Number associated with the user
     * @return a list of Cycle objects representing the cycle history for the specified user and MDN
     */
    @GetMapping("/history")
    public List<Cycle> getCycleHistory(@RequestParam String userId, @RequestParam String mdn) {
        return cycleService.findCycles(List.of(
            CycleCriteria.filterByUserId(userId),
            CycleCriteria.filterByMdn(mdn)
        )
        );
    }

    /**
     * Transfers the specified MDN (Mobile Directory Number) from one user to another.
     *
     * @param mdn the mobile directory number to be transferred
     * @param fromUserId the ID of the user from whom the MDN is being transferred
     * @param toUserId the ID of the user to whom the MDN is being transferred
     */
    @PostMapping("/transfer")
    public void transferMdn(@RequestParam String mdn, @RequestParam String fromUserId, @RequestParam String toUserId) {
        dailyUsageService.transferMdn(mdn, fromUserId, toUserId);
    }

    /**
     * Updates the daily usage for a given mobile device number (MDN) on a specific date.
     *
     * @param mdn the mobile device number for which the usage is being updated
     * @param usageDate the date of the usage to be updated
     * @param usedInMb the amount of data used in megabytes
     * @return the updated DailyUsage object
     */
    @PostMapping("/update")
    public DailyUsage updateUsage(
        @RequestParam String mdn,
        @RequestParam Date usageDate,
        @RequestParam double usedInMb) {
        return dailyUsageService.updateUsage(mdn, usageDate, usedInMb);
    }
}