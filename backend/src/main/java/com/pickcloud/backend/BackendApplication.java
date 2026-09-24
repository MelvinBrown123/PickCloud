package com.pickcloud.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the PickCloud backend application.
 *
 * The {@link SpringBootApplication} annotation enables Spring Boot auto-configuration,
 * component scanning, and configuration support for the application.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
