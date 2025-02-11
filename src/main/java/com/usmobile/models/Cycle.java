package com.usmobile.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "cycles")
public class Cycle 
{
    @Id
    private String id;
    private String mdn;
    private Date startDate;
    private Date endDate;
    private String userId;
}