package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.document.ConfirmationToken;
import com.ercanbeyen.bloggingplatform.exception.DataConflict;
import com.ercanbeyen.bloggingplatform.exception.DataNotFound;
import com.ercanbeyen.bloggingplatform.repository.ConfirmationTokenRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void createConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new DataNotFound("Confirmation token is not found"));
    }

    @Transactional
    @Override
    public void updateConfirmationToken(String token) {
        ConfirmationToken confirmationTokenInDb = getConfirmationToken(token);
        confirmationTokenInDb.setConfirmedAt(LocalDateTime.now());

        confirmationTokenRepository.save(confirmationTokenInDb);
    }
}
