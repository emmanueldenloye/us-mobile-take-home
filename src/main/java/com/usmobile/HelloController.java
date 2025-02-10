package com.usmobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

  @Autowired
  private UserRepository userRepository;
  
  @GetMapping("/hello")
  public String sayHello() {
    return "Hello from US Mobile!";
  }

  @GetMapping("/test-mongo")
  public String testMongo() {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("john.doe@example.com");
    user.setPassword("password123");

    userRepository.save(user);
    return "User saved to MongoDB!";
  }
}

