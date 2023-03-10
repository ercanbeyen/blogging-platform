package com.ercanbeyen.bloggingplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class DocumentNotFound extends RuntimeException {
    public DocumentNotFound(String message) {
        super(message);
    }
}
