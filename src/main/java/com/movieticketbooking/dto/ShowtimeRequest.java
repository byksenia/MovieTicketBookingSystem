package com.example.movieticketbookingsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowtimeRequest {
    private Long movieId;
    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer capacity;
}
