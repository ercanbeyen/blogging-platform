package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.request.auth.RegistrationRequest;
import com.ercanbeyen.bloggingplatform.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public void register(@RequestBody RegistrationRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        registrationService.register(request, httpServletRequest, httpServletResponse);
    }

    @GetMapping("/confirm")
    public ResponseEntity<Object> confirm(@RequestParam(value = "token") String token) {
        String message = registrationService.confirmToken(token);
        return ResponseEntity.ok(message);
    }
}