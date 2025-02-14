package com.usmobile.services;

import com.usmobile.models.User;
import com.usmobile.services.UserService;
import com.usmobile.repositories.UserCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import com.usmobile.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUsersByCriteria() {
        User john = new User();
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setEmail("john.doe@example.com");
        john.setPassword("password123");

        User alice = new User();
        alice.setFirstName("Alice");
        alice.setLastName("Smith");
        alice.setEmail("alice.smith@example.com");
        alice.setPassword("password123");

        Criteria criteria = new Criteria().orOperator(
            Criteria.where("firstName").is("John"),
            Criteria.where("firstName").is("Alice")
        );

        when(mongoTemplate.find(any(Query.class), eq(User.class)))
            .thenReturn(List.of(john, alice));

        List<User> result = userService.findUsers(List.of(criteria));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Alice", result.get(1).getFirstName());
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        assertEquals("john.doe@example.com", createdUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        String userId = "1";
        User existingUser = new User();
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setPassword("password123");

        User updatedUser = new User();
        updatedUser.setFirstName("Johnny");
        updatedUser.setLastName("Doe");
        updatedUser.setEmail("johnny.doe@example.com");
        updatedUser.setPassword("newpassword123");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("Johnny", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("johnny.doe@example.com", result.getEmail());
    }

    @Test
    public void testFindUserById() {
        String id = "1";
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        Criteria criteria = UserCriteria.filterById(id);
        when(mongoTemplate.findOne(new Query(criteria), User.class)).thenReturn(user);

        Optional<User> result = userService.findUserById(id);

        assertNotNull(result);
        assertEquals("John", result.get().getFirstName());
    }
}

