package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.JwtMessage;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.dto.request.auth.AuthenticationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.security.jwt.JwtService;
import com.ercanbeyen.bloggingplatform.service.AuthenticationService;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AuthorService authorService;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public void authenticate(AuthenticationRequest authenticationRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        Author author = authorService.getAuthorByUsername(authenticationRequest.getUsername());

        Map<String, String> tokenMap = jwtService.generateTokens(author);

        httpServletResponse.setHeader(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN, tokenMap.get(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN));
        httpServletResponse.setHeader(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN, tokenMap.get(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN));
    }

}
