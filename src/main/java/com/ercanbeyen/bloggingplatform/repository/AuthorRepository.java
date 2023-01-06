package com.ercanbeyen.bloggingplatform.repository;

import com.ercanbeyen.bloggingplatform.entity.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {
    Optional<Author> findPersonByEmail(String email);
}
