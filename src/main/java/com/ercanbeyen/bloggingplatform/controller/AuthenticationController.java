package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.request.auth.AuthenticationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
}
