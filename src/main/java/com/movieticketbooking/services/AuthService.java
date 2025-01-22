package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.AuthRequest;
import com.example.movieticketbookingsystem.dto.AuthResponse;
import com.example.movieticketbookingsystem.dto.UserRequest;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(User.Role.valueOf(userRequest.getRole().toUpperCase()))
                .build();
        return userRepository.save(user);
    }

    public AuthResponse login(AuthRequest authRequest) {
        System.out.println("Attempting to authenticate user: " + authRequest.getEmail());
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            System.out.println("Password mismatch for user: " + authRequest.getEmail());
            throw new RuntimeException("Invalid email or password");
        }
    
        System.out.println("User authenticated successfully: " + authRequest.getEmail());
        AuthResponse response = new AuthResponse();
        response.setMessage("Login successful!");
        response.setToken("example-token"); 
        return response;
    }
}