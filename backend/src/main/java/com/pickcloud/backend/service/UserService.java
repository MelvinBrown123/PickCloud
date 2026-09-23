package com.pickcloud.backend.service;

import com.pickcloud.backend.dto.UserResponse;
import com.pickcloud.backend.entity.User;
import com.pickcloud.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

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