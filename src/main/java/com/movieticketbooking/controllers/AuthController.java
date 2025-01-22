package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.AuthRequest;
import com.example.movieticketbookingsystem.dto.AuthResponse;
import com.example.movieticketbookingsystem.dto.UserRequest;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRequest userRequest) {
        User user = authService.register(userRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
    System.out.println("Login request received for email: " + authRequest.getEmail());
    AuthResponse response = authService.login(authRequest);
    return ResponseEntity.ok(response);
}
}
