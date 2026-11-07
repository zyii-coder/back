package com.wuy.lostandfound.exception;

/**
 * Thrown when an entity (e.g. an item) does not exist. Maps to HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
