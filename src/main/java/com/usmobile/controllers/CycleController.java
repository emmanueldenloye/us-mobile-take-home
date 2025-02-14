package com.usmobile.controllers;

import com.usmobile.models.Cycle;
import com.usmobile.services.CycleService;
import com.usmobile.repositories.CycleCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import static com.usmobile.repositories.CycleCriteria.*;

@RestController
@RequestMapping("/cycles")
public class CycleController {

    private final CycleService cycleService;

    @Autowired
    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    @GetMapping("/by-user")
    public List<Cycle> getCyclesByUser(@RequestParam String userId) {
        return cycleService.findCycles(Arrays.asList(
            CycleCriteria.filterByUserId(userId)
            )
        );
    }

    @GetMapping("/by-date-range")
    public List<Cycle> getCyclesByDateRange(
        @RequestParam Date startDate,
        @RequestParam Date endDate) {
            return cycleService.findCycles(Arrays.asList(
                CycleCriteria.filterByDateRange(startDate, endDate)
            ));
    }

    @GetMapping("/{id}")
    public Optional<Cycle> getCycleById(@PathVariable String id) {
        return cycleService.findCycleById(id);
    }
    
}
