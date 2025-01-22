package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    List<Showtime> findByMovieId(Long movieId);
    List<Showtime> findByTheater(String theater);

    // Check overlapping showtimes in the same theater
    @Query("SELECT s FROM Showtime s WHERE s.theater = :theater " +
            "AND ((:start < s.endTime AND :end > s.startTime))")
    List<Showtime> findOverlappingShowtimes(@Param("theater") String theater,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);
}
