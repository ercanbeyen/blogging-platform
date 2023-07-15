package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.constant.values.DocumentName;
import com.ercanbeyen.bloggingplatform.document.ConfirmationToken;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.repository.ConfirmationTokenRepository;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

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
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.CONFIRMATION_TOKEN, token)));
    }

    @Override
    public List<ConfirmationToken> getConfirmationTokens(String authorId) {
        Predicate<ConfirmationToken> authorPredicate = (confirmationToken) -> (authorId == null || confirmationToken.getAuthorId().equals(authorId));

        return confirmationTokenRepository.findAll()
                .stream()
                .filter(authorPredicate)
                .toList();
    }

    @Transactional
    @Override
    public void updateConfirmationToken(String token) {
        ConfirmationToken confirmationTokenInDb = getConfirmationToken(token);
        confirmationTokenInDb.setConfirmedAt(LocalDateTime.now());

        confirmationTokenRepository.save(confirmationTokenInDb);
    }

    @Transactional
    @Override
    public String deleteConfirmationToken(String id) {
        boolean doesExist = confirmationTokenRepository.findAll()
                .stream()
                .anyMatch(confirmationToken -> confirmationToken.getId().equals(id));

        if (!doesExist) {
            throw new DataNotFound(String.format(ResponseMessage.NOT_FOUND, DocumentName.CONFIRMATION_TOKEN, id));
        }

        confirmationTokenRepository.deleteById(id);

        return String.format(ResponseMessage.SUCCESSFULLY_DELETED, DocumentName.CONFIRMATION_TOKEN, id);
    }

}