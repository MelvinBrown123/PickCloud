package com.pickcloud.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes a simple endpoint used to verify that the backend is running and
 * accepting HTTP requests.
 */
@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "PickCloud backend working successfully";
    }
}
