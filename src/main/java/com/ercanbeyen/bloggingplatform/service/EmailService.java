package com.ercanbeyen.bloggingplatform.service;

public interface EmailService {
    void send(String to, String email);
    String getEmailTemplate(String name);
}
