package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.document.ConfirmationToken;
import com.ercanbeyen.bloggingplatform.dto.AuthorDto;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.exception.DataConflict;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.ConfirmationTokenService;
import com.ercanbeyen.bloggingplatform.service.EmailService;
import com.ercanbeyen.bloggingplatform.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final AuthorService authorService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    @Transactional
    @Override
    public void register(RegistrationRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Author newAuthor = authorService.createAuthor(request);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                newAuthor.getId()
        );

        confirmationTokenService.createConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailService.send(request.getEmail(), emailService.buildEmail(request.getFirstName(), link));

        httpServletResponse.setHeader("confirmation_token", token);
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationTokenInDb = confirmationTokenService.getConfirmationToken(token);

        if (confirmationTokenInDb.getConfirmedAt() != null) {
            throw new DataConflict("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationTokenInDb.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new DataConflict("Confirmation token expired");
        }


        confirmationTokenService.updateConfirmationToken(token);
        authorService.enableAuthor(confirmationTokenInDb.getAuthorId());

        return "Confirmed";
    }
}
