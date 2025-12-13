package com.example.gahona_jordan_leccion1.service;

import com.example.gahona_jordan_leccion1.dto.SupportTicketRequest;
import com.example.gahona_jordan_leccion1.dto.SupportTicketResponse;
import com.example.gahona_jordan_leccion1.exception.DuplicateTicketNumberException;
import com.example.gahona_jordan_leccion1.entities.Currency;
import com.example.gahona_jordan_leccion1.entities.SupportTicket;
import com.example.gahona_jordan_leccion1.entities.TicketStatus;
import com.example.gahona_jordan_leccion1.repository.SupportTicketRepository;
import com.example.gahona_jordan_leccion1.specification.SupportTicketSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository repository;

    @Transactional
    public SupportTicketResponse createTicket(SupportTicketRequest request) {
        // Validar que el número de ticket no exista
        if (repository.existsByTicketNumber(request.getTicketNumber())) {
            throw new DuplicateTicketNumberException(
                    "Ticket number " + request.getTicketNumber() + " already exists"
            );
        }

        SupportTicket ticket = new SupportTicket();
        ticket.setTicketNumber(request.getTicketNumber());
        ticket.setRequesterName(request.getRequesterName());
        ticket.setStatus(request.getStatus());
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setEstimatedCost(request.getEstimatedCost());
        ticket.setCurrency(request.getCurrency());
        ticket.setDueDate(request.getDueDate());
        // createdAt se genera automáticamente con @CreationTimestamp

        SupportTicket savedTicket = repository.save(ticket);
        return mapToResponse(savedTicket);
    }

    @Transactional(readOnly = true)
    public Page<SupportTicketResponse> getTicketsWithFilters(
            String q,
            TicketStatus status,
            Currency currency,
            BigDecimal minCost,
            BigDecimal maxCost,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable) {

        Specification<SupportTicket> spec = SupportTicketSpecification.withFilters(
                q, status, currency, minCost, maxCost, from, to
        );

        return repository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    private SupportTicketResponse mapToResponse(SupportTicket ticket) {
        SupportTicketResponse response = new SupportTicketResponse();
        response.setId(ticket.getId());
        response.setTicketNumber(ticket.getTicketNumber());
        response.setRequesterName(ticket.getRequesterName());
        response.setStatus(ticket.getStatus());
        response.setPriority(ticket.getPriority());
        response.setCategory(ticket.getCategory());
        response.setEstimatedCost(ticket.getEstimatedCost());
        response.setCurrency(ticket.getCurrency());
        response.setCreatedAt(ticket.getCreatedAt());
        response.setDueDate(ticket.getDueDate());
        return response;
    }
}
