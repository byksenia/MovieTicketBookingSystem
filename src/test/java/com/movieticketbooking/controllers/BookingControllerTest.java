package com.example.movieticketbookingsystem.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.movieticketbookingsystem.entity.*;
import com.example.movieticketbookingsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.SecurityMockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        ticketRepository.deleteAll();
        showtimeRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();

        // Create user
        User customer = new User(
            null,
            "Customer Name",
            "customer@example.com",
            passwordEncoder.encode("password"),
            User.Role.CUSTOMER
        );
        userRepository.save(customer);

        // Save a Movie
        Movie movie = new Movie(null, "Avatar", "Action", 162, "PG-13", 2009);
        movie = movieRepository.save(movie);

        // Save a Showtime referencing the Movie
        Showtime showtime = new Showtime();
        showtime.setTheater("Regal Cinema #1");
        showtime.setStartTime(LocalDateTime.now().plusDays(1));
        showtime.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        showtime.setCapacity(100);
        showtime.setMovie(movie);
        showtimeRepository.save(showtime);
    }

    @Test
    @Order(1)
    void bookTicket() throws Exception {
        // Retrieve the showtime we just created
        Showtime st = showtimeRepository.findAll().get(0);

        // Create request
        String bookingJson = """
        {
          "showtimeId": %d,
          "seatNumber": 10,
          "price": 12.99
        }
        """.formatted(st.getId());

        // booking for customer
        mockMvc.perform(post("/api/bookings")
                .with(httpBasic("customer@example.com", "password")) // Authenticate as CUSTOMER
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookingJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value(10))
                .andExpect(jsonPath("$.price").value(12.99));
    }
}