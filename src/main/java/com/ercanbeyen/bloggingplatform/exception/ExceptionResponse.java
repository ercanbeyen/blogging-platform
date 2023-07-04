package com.ercanbeyen.bloggingplatform.exception;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ExceptionResponse {
    private int httpStatus;
    private String message;
    private LocalDateTime createdAt;

}
