package com.usmobile.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import java.util.Date;

@Data
@Document(collection = "cycles")
@CompoundIndex(name = "user_mdn_idx", def = "{'userId': 1, 'mdn': 1}")
public class Cycle 
{
    @Id
    private String id;
    @Indexed
    private String mdn;
    private Date startDate;
    private Date endDate;
    @Indexed
    private String userId;

    public Cycle(){}
}