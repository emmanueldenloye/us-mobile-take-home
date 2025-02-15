package com.usmobile.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

@Data
@Document(collection = "Users")
@CompoundIndexes({
        @CompoundIndex(name = "firstName_lastName_idx", def = "{'firstName': 1, 'lastName': 1}")
})
public class User{
        @Id
        private String id;
        @Indexed
        private String firstName;
        @Indexed
        private String lastName;
        private String email;
        private String password;

        public User() {}
}