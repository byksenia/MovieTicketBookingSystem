package com.example.movieticketbookingsystem.service;

import com.example.movieticketbookingsystem.dto.BookingRequest;
import com.example.movieticketbookingsystem.entity.Showtime;
import com.example.movieticketbookingsystem.entity.Ticket;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.repository.ShowtimeRepository;
import com.example.movieticketbookingsystem.repository.TicketRepository;
import com.example.movieticketbookingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;

    public BookingService(TicketRepository ticketRepository,
                          ShowtimeRepository showtimeRepository,
                          UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.showtimeRepository = showtimeRepository;
        this.userRepository = userRepository;
    }

    public Ticket bookTicket(BookingRequest request, String userEmail) {
        // Find user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find showtime
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        // Check capacity
        long ticketsSold = ticketRepository.count(); 
        long alreadyBookedSeats = ticketRepository.findAll().stream()
                .filter(t -> t.getShowtime().getId().equals(showtime.getId()))
                .count();

        if (alreadyBookedSeats >= showtime.getCapacity()) {
            throw new RuntimeException("Showtime is sold out.");
        }

        // Check if seat is already booked
        boolean seatTaken = ticketRepository.existsByShowtimeIdAndSeatNumber(showtime.getId(), request.getSeatNumber());
        if (seatTaken) {
            throw new RuntimeException("Seat is already booked.");
        }

        // Book
        Ticket ticket = Ticket.builder()
                .showtime(showtime)
                .user(user)
                .seatNumber(request.getSeatNumber())
                .price(request.getPrice())
                .build();

        return ticketRepository.save(ticket);
    }
}
