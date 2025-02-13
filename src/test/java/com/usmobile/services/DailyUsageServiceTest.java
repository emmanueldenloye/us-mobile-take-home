package com.usmobile.services;

import com.usmobile.models.DailyUsage;
import com.usmobile.models.Cycle;
import com.usmobile.repositories.CycleCriteria;
import com.usmobile.repositories.DailyUsageCriteria;
import com.usmobile.controllers.UsageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DailyUsageServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private DailyUsageService dailyUsageService;
    @InjectMocks
    private CycleService cycleService;

    @Test
    public void testGetCurrentCycleUsage() {
        String userId = "user123";
        String mdn = "1234567890";
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 1000 * 60 * 60 * 24);

        DailyUsage dailyUsage1 = new DailyUsage();
        dailyUsage1.setId("1");
        dailyUsage1.setMdn(mdn);
        dailyUsage1.setUserId(userId);
        dailyUsage1.setUsageDate(startDate);
        dailyUsage1.setUsedInMb(100.0);

        DailyUsage dailyUsage2 = new DailyUsage();
        dailyUsage2.setId("2");
        dailyUsage2.setMdn(mdn);
        dailyUsage2.setUserId(userId);
        dailyUsage2.setUsageDate(new Date(startDate.getTime() + 43200000));
        dailyUsage2.setUsedInMb(200.0);

        Criteria criteria = new Criteria()
            .andOperator(
                DailyUsageCriteria.filterByUserId(userId),
                DailyUsageCriteria.filterByMdn(mdn),
                DailyUsageCriteria.filterByUsageDateBetween(startDate, endDate)
            );

        Cycle currentCycle = cycleService.findCycles(
            c -> CycleCriteria.filterByUserId(userId),
            c -> CycleCriteria.filterByMdn(mdn))
            .stream()
            .filter(cycle -> cycle.getEndDate().after(new Date()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No active cycle found")
        );
            
        List<DailyUsage> result = dailyUsageService.findDailyUsage(
            c -> DailyUsageCriteria.filterByUserId(userId),
            c -> DailyUsageCriteria.filterByMdn(mdn),
            c -> DailyUsageCriteria.filterByUsageDateBetween(currentCycle.getStartDate(), currentCycle.getEndDate()
        ));
        
        when(mongoTemplate.find(new Query(criteria), DailyUsage.class))
            .thenReturn(Arrays.asList(dailyUsage1, dailyUsage2));

        assertEquals(2, result.size());
        Set<Double> mbs = new HashSet<>(Arrays.asList(100.0, 200.0));
        assertEquals(mbs, result.stream().map(DailyUsage::getUsedInMb).collect(Collectors.toSet()));
            
    }

    @Test
    public void testGetCycleHistory() {
        String userId = "user123";
        String mdn = "1234567890";

        Cycle cycle1 = new Cycle();
        cycle1.setId("1");
        cycle1.setMdn(mdn);
        cycle1.setStartDate(new Date());
        cycle1.setEndDate(new Date());
        cycle1.setUserId(userId);

        Cycle cycle2 = new Cycle();
        cycle2.setId("2");
        cycle2.setMdn(mdn);
        cycle2.setStartDate(new Date());
        cycle2.setEndDate(new Date());
        cycle2.setUserId(userId);

        Criteria criteria = new Criteria()
            .andOperator(
                CycleCriteria.filterByUserId(userId),
                CycleCriteria.filterByMdn(mdn)
            );
        
        when(mongoTemplate.find(new Query(criteria), Cycle.class))
            .thenReturn(Arrays.asList(cycle1, cycle2));

        List<Cycle> result = cycleService.findCycles(
            c -> CycleCriteria.filterByUserId(userId),
            c -> CycleCriteria.filterByMdn(mdn)
        );

        assertEquals(2, result.size());
        Set<String> ids = new HashSet<>(Arrays.asList("1", "2"));
        assertEquals(ids, result.stream().map(Cycle::getId).collect(Collectors.toSet()));
    }

    @Test
    public void testTransferMdn() {
        String mdn = "1234567890";
        String fromUserId = "user123";
        String toUserId = "user456";

        Cycle cycle = new Cycle();
        cycle.setId("1");
        cycle.setMdn(mdn);
        cycle.setStartDate(new Date());
        cycle.setEndDate(new Date());
        cycle.setUserId(fromUserId);

        assertEquals(fromUserId, cycle.getUserId());

        when(mongoTemplate.find(new Query(CycleCriteria.filterByMdn(mdn).andOperator(CycleCriteria.filterByUserId(fromUserId))), Cycle.class))
            .thenReturn(Collections.singletonList(cycle));
        
        DailyUsage usage = new DailyUsage();
        usage.setId("1");
        usage.setMdn(mdn);
        usage.setUserId(fromUserId);
        usage.setUsageDate(new Date());
        usage.setUsedInMb(100.0);

        assertEquals(fromUserId, usage.getUserId());

        when(mongoTemplate.find(new Query(DailyUsageCriteria.filterByMdn(mdn).andOperator(DailyUsageCriteria.filterByUserId(fromUserId))), DailyUsage.class))
            .thenReturn(Collections.singletonList(usage));
        
        dailyUsageService.transferMdn(mdn, fromUserId, toUserId);

        assertEquals(toUserId, cycle.getUserId());
        assertEquals(toUserId, usage.getUserId());
    }

    public void testUpdatedUsage() {
        String mdn = "1234567890";
        Date usageDate = new Date();
        double usedInMb = 150.0;

        DailyUsage usage = new DailyUsage();
        usage.setId("1");
        usage.setMdn(mdn);
        usage.setUserId("user123");
        usage.setUsageDate(usageDate);
        usage.setUsedInMb(100.0);

        when(mongoTemplate.find(new Query(DailyUsageCriteria.filterByMdn(mdn).andOperator(DailyUsageCriteria.filterByUsageDate(usageDate))), DailyUsage.class))
            .thenReturn(Collections.singletonList(usage));

        DailyUsage updatedUsage =
            dailyUsageService.updateUsage(mdn, usageDate, usedInMb);

        assertEquals(150.0, updatedUsage.getUsedInMb());
    }
}
