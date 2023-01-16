package com.ercanbeyen.bloggingplatform.exception;

import com.ercanbeyen.bloggingplatform.document.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DocumentNotFound.class)
    public ResponseEntity<?> documentNotFoundExceptionHandler(Exception exception) {
        Response<Object> response = Response.builder()
                .success(false)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DocumentForbidden.class)
    public ResponseEntity<?> documentForbiddenException(Exception exception) {
        Response<Object> response = Response.builder()
                .success(false)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DocumentConflict.class)
    public ResponseEntity<?> documentConflictedExceptionHandler(Exception exception) {
        Response<Object> response = Response.builder()
                .success(false)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generalExceptionHandler(Exception exception) {
        Response<Object> response = Response.builder()
                .success(false)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
