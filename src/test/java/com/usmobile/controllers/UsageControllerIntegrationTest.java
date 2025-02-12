package com.usmobile.controllers;

import com.usmobile.models.Cycle;
import com.usmobile.models.DailyUsage;
import com.usmobile.repositories.CycleRepository;
import com.usmobile.repositories.DailyUsageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class UsageControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private DailyUsageRepository dailyUsageRepository;

    @Test
    public void testGetCurrentCycleUsage() throws Exception {
        var userId = "123";
        var mdn = "1234567890";
        var startDate = new Date();
        var endDate = new Date(startDate.getTime() + 86400000);

        var cycle = new Cycle();
        cycle.setMdn(mdn);
        cycle.setUserId(userId);
        cycle.setStartDate(startDate);
        cycle.setEndDate(endDate);
        cycleRepository.save(cycle);

        var usage1 = new DailyUsage();
        usage1.setMdn(mdn);
        usage1.setUserId(userId);
        usage1.setUsageDate(startDate);
        usage1.setUsedInMb(100.0);
        dailyUsageRepository.save(usage1);

        var usage2 = new DailyUsage();
        usage2.setMdn(mdn);
        usage2.setUserId(userId);
        usage2.setUsageDate(new Date(startDate.getTime() + 43200000));
        usage2.setUsedInMb(200.0);
        dailyUsageRepository.save(usage2);

        mockMvc.perform(get("/usage/current")
                .param("userId", userId)
                .param("mdn", mdn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usedInMb").value(200.0))
                .andExpect(jsonPath("$[1].usedInMb").value(100.0));
    }
}
