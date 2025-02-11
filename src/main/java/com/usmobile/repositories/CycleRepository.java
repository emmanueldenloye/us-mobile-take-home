package com.usmobile.repositories;

import com.usmobile.models.Cycle;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CycleRepository extends MongoRepository<Cycle, String>
{
    List<Cycle> findByUserIdAndMdn(String userId, String mdn);
}
