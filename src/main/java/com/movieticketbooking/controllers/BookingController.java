package com.example.movieticketbookingsystem.controller;

import com.example.movieticketbookingsystem.dto.BookingRequest;
import com.example.movieticketbookingsystem.entity.Ticket;
import com.example.movieticketbookingsystem.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Only customers can book tickets
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Ticket> bookTicket(@RequestBody BookingRequest request,
                                             @RequestHeader("X-User-Email") String userEmail) {
        Ticket ticket = bookingService.bookTicket(request, userEmail);
        return ResponseEntity.ok(ticket);
    }
}
