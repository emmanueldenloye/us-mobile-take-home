package com.usmobile.services;

import com.usmobile.models.User;
import com.usmobile.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
            
        when(userRepository.save(any(User.class))).thenReturn(user);

        var createdUser = userService.createUser(user);

        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        assertEquals("john.doe@example.com", createdUser.getEmail());
    }
}
