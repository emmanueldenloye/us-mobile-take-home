package com.usmobile;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CycleRepository extends MongoRepository<Cycle, String>
{
    List<Cycle> findByUserIdAndMdn(String userId, String mdn);
}
