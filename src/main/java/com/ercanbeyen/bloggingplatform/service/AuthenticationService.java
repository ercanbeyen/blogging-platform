package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.document.Response;
import com.ercanbeyen.bloggingplatform.dto.request.auth.AuthenticationRequest;
import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;

public interface AuthenticationService {
    Response<Object> authenticate(AuthenticationRequest request);
    Response<Object> register(RegistrationRequest request);
}
