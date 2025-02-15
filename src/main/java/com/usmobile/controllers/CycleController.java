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

    /**
     * Retrieves a list of cycles associated with a specific user.
     *
     * @param userId the ID of the user whose cycles are to be retrieved
     * @return a list of cycles associated with the specified user
     */
    @GetMapping("/by-user")
    public List<Cycle> getCyclesByUser(@RequestParam String userId) {
        return cycleService.findCycles(Arrays.asList(
            CycleCriteria.filterByUserId(userId)
            )
        );
    }

    /**
     * Retrieves a list of cycles within the specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of cycles that fall within the specified date range
     */
    @GetMapping("/by-date-range")
    public List<Cycle> getCyclesByDateRange(
        @RequestParam Date startDate,
        @RequestParam Date endDate) {
            return cycleService.findCycles(Arrays.asList(
                CycleCriteria.filterByDateRange(startDate, endDate)
            ));
    }

    /**
     * Retrieves a Cycle by its ID.
     *
     * @param id the ID of the Cycle to retrieve
     * @return an Optional containing the Cycle if found, or an empty Optional if not found
     */
    @GetMapping("/{id}")
    public Optional<Cycle> getCycleById(@PathVariable String id) {
        return cycleService.findCycleById(id);
    }
    
}
