package com.pickcloud.backend.exception;
/**
 * Represents an error produced when a requested resource
 * cannot be found in the database.
 *
 * Examples include a missing product, business, or category.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message){
        super(message);
    }
}
