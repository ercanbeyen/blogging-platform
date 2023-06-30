package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @KafkaListener(topics = NotificationMessage.POST_NOTIFICATION, groupId = "group-id")
    public void consumePostNotification(String message) {
        log.info(String.format("Message receiver\n %s", message));
    }
}
