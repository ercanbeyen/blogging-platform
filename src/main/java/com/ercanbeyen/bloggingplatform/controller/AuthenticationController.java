package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.request.auth.LoginRequest;
import com.ercanbeyen.bloggingplatform.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public void login(@RequestBody @Validated LoginRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        authenticationService.authenticate(request, httpServletRequest, httpServletResponse);
    }

    @GetMapping("/token-refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @GetMapping("/update-password")
    public void updatePassword(@RequestParam(value = "user") String username) {
        authenticationService.updatePassword(username);
    }

}
