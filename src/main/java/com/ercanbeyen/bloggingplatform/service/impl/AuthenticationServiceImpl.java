package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.dto.request.auth.AuthenticationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.security.jwt.JwtService;
import com.ercanbeyen.bloggingplatform.service.AuthenticationService;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthorService authorService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Author author = authorService.getAuthorByUsername(request.getUsername());
        return jwtService.generateToken(author);
    }

    @Override
    public String register(RegistrationRequest request) {
        Author newAuthor = authorService.createAuthor(request);
        return jwtService.generateToken(newAuthor);
    }
}
