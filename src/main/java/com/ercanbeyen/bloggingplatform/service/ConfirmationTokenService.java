package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.ConfirmationToken;

public interface ConfirmationTokenService {
    void createConfirmationToken(ConfirmationToken confirmationToken);
    ConfirmationToken getConfirmationToken(String token);
    void updateConfirmationToken(String token);
}
