package com.usmobile.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import lombok.Data;

@Data
@Document(collection = "daily_usage")
public class DailyUsage {
    @Id 
    private String id;
    private String mdn;
    private String userId;
    private Date usageDate;
    private Double usedInMb;

    public DailyUsage() {}
}