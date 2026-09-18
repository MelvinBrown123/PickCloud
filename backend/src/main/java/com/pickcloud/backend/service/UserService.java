package com.pickcloud.backend.service;

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
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}