package com.usmobile.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import java.util.Date;
import lombok.Data;

@Data
@Document(collection = "daily_usage")
@CompoundIndexes({
    @CompoundIndex(name = "user_mdn_date_idx", def = "{'userId': 1, 'mdn': 1, 'usageDate': 1}")
})
public class DailyUsage {
    @Id 
    private String id;
    @Indexed
    private String mdn;
    @Indexed
    private String userId;
    @Indexed
    private Date usageDate;
    private Double usedInMb;

    public DailyUsage() {}
}