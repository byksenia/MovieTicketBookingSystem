package com.example.movieticketbookingsystem.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long showtimeId;
    private Integer seatNumber;
    private Double price;
}
