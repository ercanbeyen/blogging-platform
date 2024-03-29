package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.constant.enums.EmailTemplate;

public interface EmailService {
    void send(String subject, String to, String text);
    String buildEmail(String name, String item, EmailTemplate emailTemplate);
}
