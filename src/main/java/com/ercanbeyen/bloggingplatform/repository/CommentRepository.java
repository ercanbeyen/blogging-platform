package com.ercanbeyen.bloggingplatform.repository;

import com.ercanbeyen.bloggingplatform.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

}
