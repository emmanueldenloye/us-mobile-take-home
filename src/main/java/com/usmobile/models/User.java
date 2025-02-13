package com.usmobile.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "Users")
public class User{
        @Id
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        public User() {}
}