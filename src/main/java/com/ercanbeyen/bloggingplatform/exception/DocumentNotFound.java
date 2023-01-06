package com.ercanbeyen.bloggingplatform.exception;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class DocumentNotFound extends RuntimeException {
    public DocumentNotFound(String message) {
        super(message);
    }
}
