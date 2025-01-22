package com.example.movieticketbookingsystem.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
