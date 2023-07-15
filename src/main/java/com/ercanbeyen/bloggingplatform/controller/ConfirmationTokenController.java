package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.document.ConfirmationToken;
import com.ercanbeyen.bloggingplatform.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/confirmation-tokens")
public class ConfirmationTokenController {
    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping
    public ResponseEntity<Object> getConfirmationTokens(@RequestParam(required = false) String authorId) {
        List<ConfirmationToken> confirmationTokens = confirmationTokenService.getConfirmationTokens(authorId);
        return ResponseEntity.ok(confirmationTokens);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteConfirmationToken(@PathVariable("id") String id) {
        String message = confirmationTokenService.deleteConfirmationToken(id);
        return ResponseEntity.ok(message);
    }


}
