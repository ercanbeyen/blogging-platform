package com.ercanbeyen.bloggingplatform.repository;

import com.ercanbeyen.bloggingplatform.entity.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String> {
    Optional<Author> findByUsername(String username);
    List<Author> findByUsernameOrEmail(String username, String email);
}
