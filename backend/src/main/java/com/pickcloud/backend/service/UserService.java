package com.pickcloud.backend.service;

import com.pickcloud.backend.dto.UserResponse;
import com.pickcloud.backend.entity.User;
import com.pickcloud.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains user-related application logic and converts User entities into safe DTOs.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users and converts every entity into UserResponse before returning it.
     * This prevents the password field from being exposed by the controller.
     */
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                // Equivalent to: .map(user -> toResponse(user))
                .map(this::toResponse)
                .toList();
    }

    /** Converts the database entity into the subset of user data exposed by the API. */
    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();

        response.setIdUsuario(user.getIdUsuario());
        response.setIdRol(user.getRol().getIdRol());
        response.setRol(user.getRol().getDescripcion());
        response.setNombre(user.getNombre());
        response.setCorreo(user.getCorreo());

        return response;
    }
}
