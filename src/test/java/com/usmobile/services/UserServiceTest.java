package com.usmobile.services;

import com.usmobile.models.User;
import com.usmobile.repositories.UserCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindUsersByEmail() {
        // Arrange
        String email = "john.doe@example.com";
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(email);
        user.setPassword("password123");

        Criteria criteria = UserCriteria.filterByEmail(email);
        when(mongoTemplate.find(new Query(criteria), User.class))
                .thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = userService.findUsers(c -> UserCriteria.filterByEmail(email));

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    public void testFindUserById() {
        // Arrange
        String id = "1";
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        Criteria criteria = UserCriteria.filterById(id);
        when(mongoTemplate.findOne(new Query(criteria), User.class))
                .thenReturn(user);

        // Act
        Optional<User> result = userService.findUserById(id);

        // Assert
        assertEquals("John", result.get().getFirstName());
    }
}
