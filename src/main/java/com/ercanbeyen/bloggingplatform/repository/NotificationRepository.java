package com.ercanbeyen.bloggingplatform.repository;

import com.ercanbeyen.bloggingplatform.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

}
