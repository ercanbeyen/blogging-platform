package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.request.auth.AuthenticationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;

public interface AuthenticationService {
    String authenticate(AuthenticationRequest request);
    String register(RegistrationRequest request);
}
