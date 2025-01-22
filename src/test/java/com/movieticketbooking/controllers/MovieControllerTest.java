package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.entity.Movie;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.User.Role;
import com.example.movieticketbookingsystem.repository.MovieRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        movieRepository.deleteAll();
        userRepository.deleteAll();

        User admin = new User(
            null, 
            "Admin Name", 
            "admin_user@gmail.com", 
            passwordEncoder.encode("password"), 
            Role.ADMIN
        );
        userRepository.save(admin);
    }

    @Test
    @Order(1)
    void testCreateMovie() throws Exception {
        String movieJson = """
        {
          "title": "Inception",
          "genre": "Sci-Fi",
          "duration": 148,
          "rating": "PG-13",
          "releaseYear": 2010
        }
        """;

        mockMvc.perform(post("/api/movies")
                .with(httpBasic("admin@example.com", "password")) // authenticate as admin
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    @Order(2)
    void testGetAllMovies() throws Exception {
        movieRepository.save(new Movie(null, "Matrix", "Action", 136, "R", 1999));

        mockMvc.perform(get("/api/movies")
                .with(httpBasic("admin@example.com", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Matrix"));
    }

    @Test
    @Order(3)
    void testGetMovieById() throws Exception {
        Movie saved = movieRepository.save(new Movie(null, "Toy Story", "Animation", 81, "G", 1995));

        mockMvc.perform(get("/api/movies/{id}", saved.getId())
                .with(httpBasic("admin@example.com", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Toy Story"));
    }

    @Test
    @Order(4)
    void testUpdateMovie() throws Exception {
        Movie saved = movieRepository.save(new Movie(null, "Interstellar", "Sci-Fi", 169, "PG-13", 2014));

        String updatedJson = """
        {
          "title": "Interstellar Updated",
          "genre": "Sci-Fi",
          "duration": 169,
          "rating": "PG-13",
          "releaseYear": 2014
        }
        """;

        mockMvc.perform(put("/api/movies/{id}", saved.getId())
                .with(httpBasic("admin@example.com", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Interstellar Updated"));
            }
        
            @Test
            @Order(5)
            void testDeleteMovie() throws Exception {
                Movie saved = movieRepository.save(new Movie(null, "Avatar", "Action", 162, "PG-13", 2009));
        
                mockMvc.perform(delete("/api/movies/{id}", saved.getId())
                        .with(httpBasic("admin@example.com", "password")))
                        .andExpect(status().isNoContent());
        
                assertFalse(movieRepository.findById(saved.getId()).isPresent());
            }
        }