package com.ercanbeyen.bloggingplatform.repository;

import com.ercanbeyen.bloggingplatform.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, String> {
    Optional<Person> findPersonByEmail(String email);
}
