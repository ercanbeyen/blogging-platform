package com.ercanbeyen.bloggingplatform.repository;

import com.ercanbeyen.bloggingplatform.document.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

}
