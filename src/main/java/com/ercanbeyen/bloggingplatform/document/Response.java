package com.ercanbeyen.bloggingplatform.document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {
    private boolean success;
    private String message;
    private T data;
}
