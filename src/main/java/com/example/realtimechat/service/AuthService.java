package com.example.realtimechat.service;

import com.example.realtimechat.dto.AuthResponse;
import com.example.realtimechat.dto.LoginRequest;
import com.example.realtimechat.dto.RegisterRequest;
import com.example.realtimechat.entity.User;
import com.example.realtimechat.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        User user = new User(
                request.getFullName(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        User savedUser = userRepository.save(user);
        return new AuthResponse(savedUser.getId(), savedUser.getFullName(), savedUser.getUsername(), "Registration successful.");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        return new AuthResponse(user.getId(), user.getFullName(), user.getUsername(), "Login successful.");
    }
}
