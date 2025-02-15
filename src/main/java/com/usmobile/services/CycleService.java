package com.usmobile.services;

import com.usmobile.models.Cycle;
import com.usmobile.repositories.CycleCriteria;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.Date;
import java.util.Arrays;

@Service
public class CycleService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CycleService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private List<Cycle> executeQuery(Criteria criteria)
    {
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Cycle.class);
    }

    private Criteria and(Criteria... criteria) {
        return new Criteria().andOperator(criteria);
    }

    public List<Cycle> findCycles(List<Criteria> filters) {
        Criteria combinedCriteria = and(
            filters.toArray(new Criteria[0])
        );
        return executeQuery(combinedCriteria);
    }

    public Optional<Cycle> findCycleById(String id) {
        Criteria criteria = CycleCriteria.filterById(id);
        Query query = new Query(criteria);
        return Optional.ofNullable(mongoTemplate.findOne(query, Cycle.class));
    }
}
