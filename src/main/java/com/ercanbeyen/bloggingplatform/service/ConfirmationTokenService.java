package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.ConfirmationToken;

import java.util.List;

public interface ConfirmationTokenService {
    void createConfirmationToken(ConfirmationToken confirmationToken);
    ConfirmationToken getConfirmationToken(String token);
    List<ConfirmationToken> getConfirmationTokens(String authorId);
    void updateConfirmationToken(String token);
    String deleteConfirmationToken(String id);
}
