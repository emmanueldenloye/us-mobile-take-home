package com.usmobile.services;


import com.usmobile.models.User;
import com.usmobile.services.UserService;
import com.usmobile.services.DailyUsageService;
import com.usmobile.repositories.UserCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.usmobile.models.DailyUsage;
import com.usmobile.models.Cycle;
import com.usmobile.repositories.CycleCriteria;
import com.usmobile.repositories.DailyUsageCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import com.usmobile.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class DailyUsageServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private DailyUsageService dailyUsageService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
    }

    @Test
    public void testFindDailyUsage() {
        Criteria criteria1 = Criteria.where("field1").is("value1");
        Criteria criteria2 = Criteria.where("field2").is("value2");

        DailyUsage usage1 = new DailyUsage();
        usage1.setId("1");
        usage1.setMdn("1234567890");
        usage1.setUserId("user123");
        usage1.setUsageDate(new Date());
        usage1.setUsedInMb(100.0);

        DailyUsage usage2 = new DailyUsage();
        usage2.setId("2");
        usage2.setMdn("1234567890");
        usage2.setUserId("user123");
        usage2.setUsageDate(new Date());
        usage2.setUsedInMb(200.0);

        when(mongoTemplate.find(any(Query.class), eq(DailyUsage.class)))
            .thenReturn(Arrays.asList(usage1, usage2));

        List<DailyUsage> result = dailyUsageService.findDailyUsage(Arrays.asList(criteria1, criteria2));

        assertEquals(2, result.size());
        assertEquals(100.0, result.get(0).getUsedInMb(), 0.01);
        assertEquals(200.0, result.get(1).getUsedInMb(), 0.01);
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

        when(mongoTemplate.find(new Query(CycleCriteria.filterByMdn(mdn).andOperator(CycleCriteria.filterByUserId(fromUserId))), Cycle.class))
            .thenReturn(Collections.singletonList(cycle));

        DailyUsage usage = new DailyUsage();
        usage.setId("1");
        usage.setMdn(mdn);
        usage.setUserId(fromUserId);
        usage.setUsageDate(new Date());
        usage.setUsedInMb(100.0);

        when(mongoTemplate.find(new Query(DailyUsageCriteria.filterByMdn(mdn).andOperator(DailyUsageCriteria.filterByUserId(fromUserId))), DailyUsage.class))
            .thenReturn(Collections.singletonList(usage));

        dailyUsageService.transferMdn(mdn, fromUserId, toUserId);

        verify(mongoTemplate).save(argThat(argument -> argument instanceof DailyUsage && ((DailyUsage) argument).getId().equals("1") && ((DailyUsage) argument).getUserId().equals(toUserId)));
        verify(mongoTemplate).save(argThat(argument -> argument instanceof Cycle && ((Cycle) argument).getId().equals("1") && ((Cycle) argument).getUserId().equals(toUserId)));
    }

    @Test
    public void testUpdateUsage() {
        String mdn = "1234567890";
        Date usageDate = new Date();
        double usedInMb = 150.0;

        DailyUsage usage = new DailyUsage();
        usage.setId("1");
        usage.setMdn(mdn);
        usage.setUserId("user123");
        usage.setUsageDate(usageDate);
        usage.setUsedInMb(100.0);

        when(mongoTemplate.findOne(new Query(DailyUsageCriteria.filterByMdn(mdn).andOperator(DailyUsageCriteria.filterByUsageDateWithin12Hours(usageDate))), DailyUsage.class))
            .thenReturn(usage);
            
        DailyUsage updatedUsage = dailyUsageService.updateUsage(mdn, usageDate, usedInMb);
        
        assertNotNull(updatedUsage);
        assertEquals(150.0, updatedUsage.getUsedInMb(), 0.01);
        verify(mongoTemplate).save(argThat(argument -> argument instanceof DailyUsage && ((DailyUsage) argument).getId().equals("1") && ((DailyUsage) argument).getUsedInMb() == 150.0));

    }
}