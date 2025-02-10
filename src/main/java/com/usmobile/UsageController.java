package com.usmobile;

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
}