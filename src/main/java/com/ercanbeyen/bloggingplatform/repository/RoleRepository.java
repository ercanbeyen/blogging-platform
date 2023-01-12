package com.ercanbeyen.bloggingplatform.repository;

import com.ercanbeyen.bloggingplatform.document.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

}
