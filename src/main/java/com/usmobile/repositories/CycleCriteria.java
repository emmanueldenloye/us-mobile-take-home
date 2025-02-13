package com.usmobile.repositories;

import org.springframework.data.mongodb.core.query.Criteria;
import java.util.Date;

public class CycleCriteria {

    public static Criteria filterById(String id) {
        return Criteria.where("id").is(id);
    }

    public static Criteria filterByMdn(String mdn) {
        return Criteria.where("mdn").is(mdn);
    }

    public static Criteria filterByStartDate(Date startDate) {
        return Criteria.where("startDate").is(startDate);
    }

    public static Criteria filterByEndDate(Date endDate) {
        return Criteria.where("endDate").is(endDate);
    }

    public static Criteria filterByDateRange(Date startDate, Date endDate) {
        return Criteria.where("dateRange").gte(startDate).lte(endDate);
    }

    public static Criteria filterByUserId(String userId) {
        return Criteria.where("userId").is(userId);
    }

}
