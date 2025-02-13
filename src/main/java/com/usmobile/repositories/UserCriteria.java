package com.usmobile.repositories;

import org.springframework.data.mongodb.core.query.Criteria;

public class UserCriteria {

    public static Criteria filterById(String id) {
        return Criteria.where("id").is(id);
    }

    public static Criteria filterByFirstName(String firstName) {
        return Criteria.where("firstName").is(firstName);
    }

    public static Criteria filterByLastName(String lastName) {
        return Criteria.where("lastName").is(lastName);
    }

    public static Criteria filterByEmail(String email) {
        return Criteria.where("email").is(email);
    }

    public static Criteria filterByPassword(String password) {
        return Criteria.where("password").is(password);
    }

}
