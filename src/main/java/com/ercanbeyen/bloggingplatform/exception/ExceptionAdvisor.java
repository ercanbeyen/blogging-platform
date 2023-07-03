package com.ercanbeyen.bloggingplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DocumentNotFound.class)
    public ResponseEntity<?> documentNotFoundExceptionHandler(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DocumentForbidden.class)
    public ResponseEntity<?> documentForbiddenException(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.FORBIDDEN.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DocumentConflict.class)
    public ResponseEntity<?> documentConflictedExceptionHandler(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.CONFLICT.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generalExceptionHandler(Exception exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
