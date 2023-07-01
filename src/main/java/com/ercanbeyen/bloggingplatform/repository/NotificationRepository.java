package com.ercanbeyen.bloggingplatform.repository;

import com.ercanbeyen.bloggingplatform.document.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

}
