package com.tienda_back.model.exception;

public class JsonNullException extends RuntimeException {
    public JsonNullException(String message) {
        super(message);
    }
}
