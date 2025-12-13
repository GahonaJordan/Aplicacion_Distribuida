package com.example.gahona_jordan_leccion1.repository;

import com.example.gahona_jordan_leccion1.entities.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long>,
        JpaSpecificationExecutor<SupportTicket> {
    boolean existsByTicketNumber(String ticketNumber);
}
