package com.usmobile.controllers;

import com.usmobile.models.Cycle;
import com.usmobile.models.DailyUsage;
import com.usmobile.services.UsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usage")
public class UsageController {

    @Autowired
    private UsageService usageService;

    @GetMapping("/current")
    public List<DailyUsage> getCurrentCycleUsage(@RequestParam String userId, @RequestParam String mdn) {
        return usageService.getCurrentCycleUsage(userId, mdn);
    }

    @GetMapping("/history")
    public List<Cycle> getCycleHistory(@RequestParam String userId, @RequestParam String mdn) {
        return usageService.getCycleHistory(userId, mdn);
    }

    @PostMapping("/transfer")
    public void transferMdn(@RequestParam String mdn, @RequestParam String fromUserId, @RequestParam String toUserId) {
        usageService.transferMdn(mdn, fromUserId, toUserId);
    }
}