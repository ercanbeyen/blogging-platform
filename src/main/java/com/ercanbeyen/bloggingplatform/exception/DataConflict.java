package com.ercanbeyen.bloggingplatform.exception;

public class DataConflict extends RuntimeException {
    public DataConflict(String message) {
        super(message);
    }
}
