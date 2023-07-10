package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RegistrationService {
    void register(RegistrationRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    String confirmToken(String token);
}
