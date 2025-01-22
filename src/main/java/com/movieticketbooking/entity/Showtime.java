package com.example.movieticketbookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String theater;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer capacity; 

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
