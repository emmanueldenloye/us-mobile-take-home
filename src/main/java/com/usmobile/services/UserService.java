package com.usmobile.services;

import com.usmobile.models.User;
import com.usmobile.repositories.UserCriteria;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.usmobile.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.function.Function;
import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public UserService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private List<User> executeQuery(Criteria criteria)
    {
        Query query = new Query(criteria);
        return mongoTemplate.find(query, User.class);
    }

    private Criteria and(Criteria... criteria) {
        return new Criteria().andOperator(criteria);
    }

    public List<User> findUsers(List<Criteria> filters) {
        Criteria combinedCriteria = and(
            filters.toArray(new Criteria[0])
        );
        return executeQuery(combinedCriteria);
    }

    public Optional<User> findUserById(String id) {
        Criteria criteria = UserCriteria.filterById(id);
        Query query = new Query(criteria);
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String userId, User updatedUser) {
        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        return userRepository.save(user);
    }
}
