package com.pickcloud.backend.exception;
/**
 * Represents a conflict caused by attempting to create
 * a resource that violates an application uniqueness rule.
 * For example, a business cannot register two products
 * using the same SKU.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message){
        super(message);
    }
}
