package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.request.auth.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    void authenticate(LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;
    void updatePassword(String username);
}
