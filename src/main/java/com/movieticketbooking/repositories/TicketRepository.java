package com.example.movieticketbookingsystem.repository;

import com.example.movieticketbookingsystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByShowtimeIdAndSeatNumber(Long showtimeId, Integer seatNumber);
}
