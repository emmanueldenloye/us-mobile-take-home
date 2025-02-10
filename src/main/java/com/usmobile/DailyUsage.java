package com.usmobile;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "daily_usage")
public class DailyUsage {
    @Id 
    private String id;
    private String mdn;
    private String userId;
    private Date usageDate;
    private Double usedInMb;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMdn() {
        return this.mdn;
    }

    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getUsageDate() {
        return this.usageDate;
    }

    public void setUsageDate(Date usageDate) {
        this.usageDate = usageDate;
    }

    public Double getUsedInMb() {
        return this.usedInMb;
    }

    public void setUsedInMb(Double usedInMb) {
        this.usedInMb = usedInMb;
    }
}