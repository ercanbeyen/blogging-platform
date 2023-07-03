package com.ercanbeyen.bloggingplatform.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ExceptionResponse {
    private int httpStatus;
    private String message;
    private LocalDateTime createdAt;

}
