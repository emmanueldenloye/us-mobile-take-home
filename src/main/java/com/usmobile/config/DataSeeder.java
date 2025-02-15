package com.usmobile;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.usmobile.models.User;
import com.usmobile.models.Cycle;
import com.usmobile.models.DailyUsage;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(MongoTemplate mongoTemplate) {
        return args -> {
            int numberOfDocuments = 1000;

            for (int i = 0; i < numberOfDocuments; i++) {
                User user = new User();
                user.setFirstName(RandomStringUtils.randomAlphabetic(10));
                user.setLastName(RandomStringUtils.randomAlphabetic(10));
                user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@example.com");
                user.setPassword(RandomStringUtils.randomAlphabetic(10));
                mongoTemplate.save(user);
            }

            for (int i = 0; i < numberOfDocuments; i++) {
                Cycle cycle = new Cycle();
                cycle.setMdn(RandomStringUtils.randomNumeric(10));
                Date startDate = DateUtils.addDays(new Date(), -Integer.parseInt(RandomStringUtils.randomNumeric(1, 7)));
                Date endDate = DateUtils.addDays(startDate, Integer.parseInt(RandomStringUtils.randomNumeric(1, 7)));
                cycle.setStartDate(startDate);
                cycle.setEndDate(endDate);
                cycle.setUserId(RandomStringUtils.randomNumeric(10));
                mongoTemplate.save(cycle);
            }

            for (int i = 0; i < numberOfDocuments; i++) {
                DailyUsage dailyUsage = new DailyUsage();
                dailyUsage.setMdn(RandomStringUtils.randomNumeric(10));
                dailyUsage.setUserId(RandomStringUtils.randomNumeric(10));
                dailyUsage.setUsageDate(DateUtils.addDays(new Date(), -Integer.parseInt(RandomStringUtils.randomNumeric(1, 7))));
                dailyUsage.setUsedInMb(Double.parseDouble(RandomStringUtils.randomNumeric(1, 1024)));
                mongoTemplate.save(dailyUsage);
            }
        };
    }

}
