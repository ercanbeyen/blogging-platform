package com.ercanbeyen.bloggingplatform.exception.data;

public class DataConflict extends RuntimeException {
    public DataConflict(String message) {
        super(message);
    }
}
