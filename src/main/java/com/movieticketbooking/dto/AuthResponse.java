package com.example.movieticketbookingsystem.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String message;
    private String token; // if using JWT
}
