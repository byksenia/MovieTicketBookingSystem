package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.ShowtimeRequest;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.Showtime;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.ShowtimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository, 
                           MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    public Showtime createShowtime(ShowtimeRequest request) {
        // Validate the movie
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        boolean hasOverlap = !showtimeRepository.findOverlappingShowtimes(
                request.getTheater(), 
                request.getStartTime(), 
                request.getEndTime()).isEmpty();
        if (hasOverlap) {
            throw new RuntimeException("Showtime overlaps with existing showtime in the same theater.");
        }

        Showtime showtime = Showtime.builder()
                .movie(movie)
                .theater(request.getTheater())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .capacity(request.getCapacity())
                .build();
        return showtimeRepository.save(showtime);
    }

    public Showtime updateShowtime(Long id, ShowtimeRequest request) {
        Optional<Showtime> optionalShowtime = showtimeRepository.findById(id);
        if (optionalShowtime.isPresent()) {
            Showtime showtime = optionalShowtime.get();
            showtime.setTheater(request.getTheater());
            showtime.setStartTime(request.getStartTime());
            showtime.setEndTime(request.getEndTime());
            showtime.setCapacity(request.getCapacity());
            
            Movie movie = movieRepository.findById(request.getMovieId())
                    .orElseThrow(() -> new RuntimeException("Movie not found"));
            showtime.setMovie(movie);
            
            return showtimeRepository.save(showtime);
        }
        throw new RuntimeException("Showtime not found");
    }

    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }

    public List<Showtime> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    public List<Showtime> getShowtimesByTheater(String theater) {
        return showtimeRepository.findByTheater(theater);
    }

    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
    }
}
