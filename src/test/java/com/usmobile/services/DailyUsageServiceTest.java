package com.usmobile.services;

import com.usmobile.models.DailyUsage;
import com.usmobile.models.Cycle;
import com.usmobile.repositories.CycleCriteria;
import com.usmobile.repositories.DailyUsageCriteria;
import com.usmobile.controllers.UsageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@DataMongoTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DailyUsageServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DailyUsageService dailyUsageService;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private UsageController usageController;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetCurrentCycleUsage() {
        String userId = "user123";
        String mdn = "1234567890";
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000);

        Cycle cycle = new Cycle();
        cycle.setId("1");
        cycle.setMdn(mdn);
        cycle.setStartDate(startDate);
        cycle.setEndDate(endDate);
        cycle.setUserId(userId);

        when(cycleService.findCycles(any(),any()))
            .thenReturn(Collections.singletonList(cycle));

        DailyUsage usage1 = new DailyUsage();
        usage1.setId("1");
        usage1.setMdn(mdn);
        usage1.setUserId(userId);
        usage1.setUsageDate(startDate);
        usage1.setUsedInMb(100.0);

        DailyUsage usage2 = new DailyUsage();
        usage2.setId("2");
        usage2.setMdn(mdn);
        usage2.setUserId(userId);
        usage2.setUsageDate(new Date(startDate.getTime() + 43200000));
        usage2.setUsedInMb(200.0);
        
        when(dailyUsageService.findDailyUsage(any(),any(),any()))
            .thenReturn(Collections.singletonList(usage1));

        List<DailyUsage> result = usageController.getCurrentCycleUsage(userId, mdn);

        assertEquals(2, result.size());
        assertEquals(100.0, result.get(0).getUsedInMb(), 0.01);
        assertEquals(200.0, result.get(1).getUsedInMb(), 0.01);
            
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
