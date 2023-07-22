package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.enums.EmailTemplate;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.document.ConfirmationToken;
import com.ercanbeyen.bloggingplatform.dto.ConfirmationTokenDto;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.exception.data.DataConflict;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.ConfirmationTokenService;
import com.ercanbeyen.bloggingplatform.service.EmailService;
import com.ercanbeyen.bloggingplatform.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final AuthorService authorService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    @Transactional
    @Override
    public void register(RegistrationRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        boolean authorExists = authorService.authorExists(request.getUsername());
        Author registeredAuthor;

        if (!authorExists) {
            registeredAuthor = authorService.createAuthor(request);
            log.info("Author " + registeredAuthor.getId() + " is created");
        } else {
            registeredAuthor = authorService.findAuthorByUsername(request.getUsername());
            List<ConfirmationTokenDto> confirmationTokenDtoList = confirmationTokenService.getConfirmationTokens(registeredAuthor.getId());

            for (ConfirmationTokenDto confirmationTokenDto : confirmationTokenDtoList) {
                if (confirmationTokenDto.getConfirmedAt() != null) {
                    throw new DataConflict(ResponseMessage.ALREADY_CONFIRMED);
                }
            }

            log.info("Author " + registeredAuthor.getId()  + " has not been confirmed. So, registration process continues");
        }

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                registeredAuthor.getId()
        );

        confirmationTokenService.createConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailService.send("Registration", request.getEmail(), emailService.buildEmail(request.getFirstName(), link, EmailTemplate.REGISTRATION));

        httpServletResponse.setHeader("confirmation_token", token);
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationTokenDto confirmationTokenInDb = confirmationTokenService.getConfirmationToken(token);

        if (confirmationTokenInDb.getConfirmedAt() != null) {
            throw new DataConflict(ResponseMessage.ALREADY_CONFIRMED);
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
