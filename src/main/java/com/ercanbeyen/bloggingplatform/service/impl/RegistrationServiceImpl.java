package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.enums.EmailTemplate;
import com.ercanbeyen.bloggingplatform.constant.values.TokenTime;
import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.entity.ConfirmationToken;
import com.ercanbeyen.bloggingplatform.dto.ConfirmationTokenDto;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.exception.data.DataConflict;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.ConfirmationTokenService;
import com.ercanbeyen.bloggingplatform.service.EmailService;
import com.ercanbeyen.bloggingplatform.service.RegistrationService;
import com.ercanbeyen.bloggingplatform.util.RandomUtil;
import com.ercanbeyen.bloggingplatform.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private static final String CONFIRMATION_URL = "http://localhost:8080/api/v1/registration/confirm?token=";
    private final AuthorService authorService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    @Transactional
    @Override
    public void register(RegistrationRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Author registeredAuthor = registerAuthor(request);
        String token = createConfirmationToken(registeredAuthor.getId());
        sendEmail(request, token);
        httpServletResponse.setHeader("confirmation_token", token);
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationTokenDto confirmationTokenInDb = confirmationTokenService.getConfirmationToken(token);

        if (confirmationTokenInDb.getConfirmedAt() != null) {
            throw new DataConflict("Account already confirmed");
        }

        LocalDateTime expiredAt = confirmationTokenInDb.getExpiresAt();

        if (expiredAt.isBefore(TimeUtil.calculateNow())) {
            throw new DataConflict("Confirmation token expired");
        }

        confirmationTokenService.updateConfirmationToken(token);
        authorService.enableAuthor(confirmationTokenInDb.getAuthorId());

        return "Confirmed";
    }

    private Author registerAuthor(RegistrationRequest request) {
        boolean authorExists = authorService.authorExistsByUsername(request.getUsername());
        Author registeredAuthor;

        if (!authorExists) {
            registeredAuthor = authorService.createAuthor(request);
            log.info("Author {} is created", registeredAuthor.getId());
        } else {
            registeredAuthor = authorService.findAuthorByUsername(request.getUsername());
            checkConfirmationTokens(registeredAuthor.getId());
            log.info("Author {} has not been confirmed. So, registration process continues", registeredAuthor.getId());
        }

        return registeredAuthor;
    }

    private void checkConfirmationTokens(String authorId) {
        List<ConfirmationTokenDto> confirmationTokenDtoList = confirmationTokenService.getConfirmationTokens(authorId);

        for (ConfirmationTokenDto confirmationTokenDto : confirmationTokenDtoList) {
            if (confirmationTokenDto.getConfirmedAt() != null) {
                throw new DataConflict("Account already registered");
            }
        }
    }

    private void sendEmail(RegistrationRequest request, String token) {
        String link = CONFIRMATION_URL + token;
        String email = emailService.buildEmail(request.getFirstName(), link, EmailTemplate.REGISTRATION);
        emailService.send("Registration", request.getEmail(), email);
    }

    private String createConfirmationToken(String authorId) {
        String token = RandomUtil.getRandomString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                TimeUtil.calculateNow(),
                TimeUtil.calculateNow().plusMinutes(TokenTime.CONFIRMATION_TOKEN),
                authorId
        );

        confirmationTokenService.createConfirmationToken(confirmationToken);
        return token;
    }
}
