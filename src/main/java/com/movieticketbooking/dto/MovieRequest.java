package com.example.movieticketbookingsystem.dto;

import lombok.Data;

@Data
public class MovieRequest {
    private String title;
    private String genre;
    private Integer duration;
    private String rating;
    private Integer releaseYear;
}
