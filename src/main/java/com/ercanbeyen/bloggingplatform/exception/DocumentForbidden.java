package com.ercanbeyen.bloggingplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DocumentForbidden extends RuntimeException {
    public DocumentForbidden(String message) {
        super(message);
    }
}
