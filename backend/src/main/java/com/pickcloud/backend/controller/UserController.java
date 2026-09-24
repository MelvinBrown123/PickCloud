package com.pickcloud.backend.controller;

import com.pickcloud.backend.dto.UserResponse;
import com.pickcloud.backend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for user queries.
 *
 * UserResponse is returned instead of the User entity so sensitive fields,
 * especially the password, are not exposed in the API response.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Returns all users using the safe response representation defined by UserResponse. */
    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }
}
