package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.User.Role;
import com.example.movieticketbookingsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        // Clean the database before each test
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testRegister() throws Exception {
        String json = """
        {
          "name": "John Doe",
          "email": "john@example.com",
          "password": "secret123",
          "role": "CUSTOMER"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));

        // Verify in database
        User user = userRepository.findByEmail("john@example.com").orElseThrow();
        assertTrue(passwordEncoder.matches("secret123", user.getPassword()));
    }

    @Test
    @Order(2)
    void testLogin() throws Exception {
        // Insert a user with a known bcrypt-hashed password for "password"
        User user = new User(null, "Admin User", "admin@example.com",
                passwordEncoder.encode("password"), Role.ADMIN);
        userRepository.save(user);

        String loginJson = """
        {
          "email": "admin@example.com",
          "password": "password"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful!"))
                .andExpect(jsonPath("$.token").exists());
    }
}