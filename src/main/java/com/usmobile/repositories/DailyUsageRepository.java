package com.usmobile.repositories;

import com.usmobile.models.DailyUsage;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DailyUsageRepository extends MongoRepository<DailyUsage, String>{
    List<DailyUsage> findByUserIdAndMdnAndUsageDateBetween(String userId, String mdn, Date startDate, Date endDate);
    Optional<DailyUsage> findByMdnAndUsageDate(String mdn, Date usageDate);
    List<DailyUsage> findByUserIdAndMdn(String userId,String mdn);
}
