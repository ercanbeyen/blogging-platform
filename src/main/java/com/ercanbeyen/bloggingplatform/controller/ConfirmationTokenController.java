package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.ConfirmationTokenDto;
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
        List<ConfirmationTokenDto> confirmationTokenDtoList = confirmationTokenService.getConfirmationTokens(authorId);
        return ResponseEntity.ok(confirmationTokenDtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteConfirmationToken(@PathVariable("id") String id) {
        String message = confirmationTokenService.deleteConfirmationToken(id);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteConfirmationTokens(@RequestParam("authorId") String authorId) {
        String message = confirmationTokenService.deleteConfirmationTokens(authorId);
        return ResponseEntity.ok(message);
    }

}
