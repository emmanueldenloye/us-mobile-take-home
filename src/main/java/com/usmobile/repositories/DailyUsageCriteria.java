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
    
    public static Criteria filterByExactUsageDate(Date usageDate) {
        return Criteria.where("usageDate").is(usageDate);
    }

    public static Criteria filterByUsageDateBetween(Date startDate, Date endDate) {
        return Criteria.where("usageDate").gte(startDate).lte(endDate);
    }

    public static Criteria filterByUsageDateWithin12Hours(Date usageDate) {
        Date startDate = new Date(usageDate.getTime() - 12 * 60 * 60 * 1000);
        Date endDate = new Date(usageDate.getTime() + 12 * 60 * 60 * 1000);
        return Criteria.where("usageDate").gte(startDate).lte(endDate);
    }
}
