package com.wuy.lostandfound.exception;

/**
 * Thrown for expected business-validation failures (duplicate username, not owner, etc.).
 * Maps to HTTP 400.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
