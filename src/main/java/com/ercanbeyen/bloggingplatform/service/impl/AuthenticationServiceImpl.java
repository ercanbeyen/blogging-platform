package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.document.Response;
import com.ercanbeyen.bloggingplatform.document.Role;
import com.ercanbeyen.bloggingplatform.dto.request.auth.AuthenticationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.exception.DocumentNotFound;
import com.ercanbeyen.bloggingplatform.repository.AuthorRepository;
import com.ercanbeyen.bloggingplatform.repository.RoleRepository;
import com.ercanbeyen.bloggingplatform.security.jwt.JwtService;
import com.ercanbeyen.bloggingplatform.service.AuthenticationService;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthorService authorService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    @Override
    public Response<Object> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Author author = authorService.getAuthorByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(author);


        return Response.builder()
                .success(true)
                .message("Successfully authenticated")
                .data(jwtToken)
                .build();
    }

    @Override
    public Response<Object> register(RegistrationRequest request) {
        Author newAuthor = authorService.createAuthor(request);
        String jwtToken = jwtService.generateToken(newAuthor);

        return Response.builder()
                .success(true)
                .message("Successfully registered")
                .data(jwtToken)
                .build();
    }
}
