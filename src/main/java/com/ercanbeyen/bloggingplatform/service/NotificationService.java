package com.ercanbeyen.bloggingplatform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @KafkaListener(topics = "blog-notification", groupId = "group-id")
    public void consume(String message) {
        log.info(String.format("Message receiver\n %s", message));
    }
}
