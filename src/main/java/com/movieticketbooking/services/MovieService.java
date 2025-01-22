package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.MovieRequest;
import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(MovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .genre(request.getGenre())
                .duration(request.getDuration())
                .rating(request.getRating())
                .releaseYear(request.getReleaseYear())
                .build();
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, MovieRequest request) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            movie.setTitle(request.getTitle());
            movie.setGenre(request.getGenre());
            movie.setDuration(request.getDuration());
            movie.setRating(request.getRating());
            movie.setReleaseYear(request.getReleaseYear());
            return movieRepository.save(movie);
        }
        throw new RuntimeException("Movie not found");
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }
}
