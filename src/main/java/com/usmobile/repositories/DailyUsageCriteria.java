package com.usmobile.repositories;

import org.springframework.data.mongodb.core.query.Criteria;
import java.util.Date;

public class DailyUsageCriteria {


    public static Criteria filterByUserId(String userId) {
        return Criteria.where("userId").is(userId);
    }

    public static Criteria filterByMdn(String mdn) {
        return Criteria.where("mdn").is(mdn);
    }
    
    public static Criteria filterByUsageDate(Date usageDate) {
        return Criteria.where("usageDate").is(usageDate);
    }


    public static Criteria filterByUsageDateBetween(Date startDate, Date endDate) {
        return Criteria.where("dateRange").gte(startDate).lte(endDate);
    }
}
