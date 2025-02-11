package com.usmobile.services;

import com.usmobile.models.Cycle;
import com.usmobile.models.DailyUsage;
import com.usmobile.repositories.CycleRepository;
import com.usmobile.repositories.DailyUsageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsageServiceTest {

    @Mock
    private DailyUsageRepository dailyUsageRepository;

    @Mock
    private CycleRepository cycleRepository;

    @InjectMocks
    private UsageService usageService;

    @Test
    public void testGetCurrentCycleUsage()
    {
        var userId = "user123";
        var mdn = "1234567890";
        var startDate = new Date();

        int day = 86400000;
        var endDate = new Date(startDate.getTime() + day);

        var cycle = new Cycle();
        cycle.setStartDate(startDate);
        cycle.setEndDate(endDate);

        var usage1 = new DailyUsage();
        usage1.setUsedInMb(100.0);

        var usage2 = new DailyUsage();
        usage2.setUsedInMb(200.0);

        when(cycleRepository.findByUserIdAndMdn(userId, mdn)).thenReturn(Arrays.asList(cycle));
        when(dailyUsageRepository.findByUserIdAndMdnAndUsageDateBetween(userId, mdn, startDate, endDate)).thenReturn(Arrays.asList(usage1, usage2));

        List<DailyUsage> usages = usageService.getCurrentCycleUsage(userId, mdn);

        assertEquals(2, usages.size());
        assertEquals(100.0, usages.get(0).getUsedInMb());
        assertEquals(200.0, usages.get(1).getUsedInMb());

    }
}
