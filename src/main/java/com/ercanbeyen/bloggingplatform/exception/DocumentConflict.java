package com.ercanbeyen.bloggingplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DocumentConflict extends RuntimeException {
    public DocumentConflict(String message) {
        super(message);
    }
}
