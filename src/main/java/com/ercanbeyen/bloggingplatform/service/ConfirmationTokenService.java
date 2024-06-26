package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.entity.ConfirmationToken;
import com.ercanbeyen.bloggingplatform.dto.ConfirmationTokenDto;

import java.util.List;

public interface ConfirmationTokenService {
    void createConfirmationToken(ConfirmationToken confirmationToken);
    ConfirmationTokenDto getConfirmationToken(String token);
    List<ConfirmationTokenDto> getConfirmationTokens(String username);
    void updateConfirmationToken(String token);
    String deleteConfirmationToken(String id);
    String deleteConfirmationTokens(String authorId);
}
