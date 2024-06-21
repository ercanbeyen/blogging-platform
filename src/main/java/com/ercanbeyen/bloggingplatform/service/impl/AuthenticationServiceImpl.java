package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.enums.EmailTemplate;
import com.ercanbeyen.bloggingplatform.constant.messages.JwtMessage;
import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.dto.request.auth.LoginRequest;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.security.jwt.JwtService;
import com.ercanbeyen.bloggingplatform.service.AuthenticationService;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.EmailService;
import com.ercanbeyen.bloggingplatform.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AuthorService authorService;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public void authenticate(LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Author author = authorService.findAuthorByUsername(loginRequest.getUsername());

        Map<String, String> tokenMap = jwtService.generateTokens(author);

        httpServletResponse.setHeader(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN, tokenMap.get(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN));
        httpServletResponse.setHeader(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN, tokenMap.get(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN));
    }

    @Override
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (JwtUtil.doesTokenNotExist(authorizationHeader)) {
            throw new DataNotFound("Refresh token is missing");
        }

        try {
            Map<String, String> tokenMap = jwtService.refreshToken(authorizationHeader);
            httpServletResponse.setHeader(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN, tokenMap.get(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN));
            httpServletResponse.setHeader(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN, tokenMap.get(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN));
        } catch (Exception exception) {
            log.error("Error while refreshing token: {}", exception.getMessage());
            httpServletResponse.setHeader(JwtMessage.HEADER_KEY_ERROR, exception.getMessage());
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value());
        } finally {
            log.info("Refresh method is terminated");
        }
    }

    @Override
    public void updatePassword(String username) {
        Author authorInDb = authorService.findAuthorByUsername(username);
        String newPassword = UUID.randomUUID().toString();
        authorService.updatePassword(username, newPassword);

        emailService.send("Password Update", authorInDb.getEmail(), emailService.buildEmail(authorInDb.getFirstName(), newPassword, EmailTemplate.PASSWORD_UPDATE));
    }

}
