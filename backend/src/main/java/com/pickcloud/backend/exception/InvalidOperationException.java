package com.pickcloud.backend.exception;

/**
 * Represents an invalid business operation that cannot be completed
 * because it violates a domain rule.
 * For example, stock cannot be reduced below zero.
 */
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {

        super(message);
    }
}
