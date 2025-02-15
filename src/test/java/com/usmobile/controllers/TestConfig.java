package com.usmobile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

import com.usmobile.models.User;
import com.usmobile.models.Cycle;
import com.usmobile.models.DailyUsage;
@Configuration
public class TestConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    CommandLineRunner seedDatabase() {
        return args -> {

            // Clear existing data
            mongoTemplate.dropCollection("Users");
            mongoTemplate.dropCollection("cycles");
            mongoTemplate.dropCollection("daily_usage");
            // Add your seed data here
            User user = new User();
            user.setId("1234567890");
            user.setFirstName("TestFirstName");
            user.setLastName("TestLastName");
            user.setEmail("test@example.com");
            user.setPassword("password123");
            
            User user2 = new User();
            user2.setId("1234567891");
            user2.setFirstName("TestFirstName2");
            user2.setLastName("TestLastName2");
            user2.setEmail("test2@example.com");
            user2.setPassword("password123");

            mongoTemplate.save(user);

            Cycle cycle = new Cycle();
            cycle.setMdn("1234567890");
            cycle.setStartDate(new Date());
            cycle.setEndDate(new Date(new Date().getTime() + 1000 * 60 * 60 * 24));
            cycle.setUserId(user.getId());

            mongoTemplate.save(cycle);

            DailyUsage dailyUsage = new DailyUsage();
            dailyUsage.setMdn("1234567890");
            dailyUsage.setUserId(user.getId());
            dailyUsage.setUsageDate(new Date());
            dailyUsage.setUsedInMb(100.0);

            mongoTemplate.save(dailyUsage);

            System.out.println("Seeding database with initial data...");
        };
    }
}