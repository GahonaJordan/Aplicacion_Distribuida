package com.example.gahona_jordan_leccion1.controller;

import com.example.gahona_jordan_leccion1.dto.SupportTicketRequest;
import com.example.gahona_jordan_leccion1.dto.SupportTicketResponse;
import com.example.gahona_jordan_leccion1.exception.InvalidDateRangeException;
import com.example.gahona_jordan_leccion1.entities.Currency;
import com.example.gahona_jordan_leccion1.entities.TicketStatus;
import com.example.gahona_jordan_leccion1.service.SupportTicketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/support-tickets")
@RequiredArgsConstructor
@Validated
public class SupportTicketController {

    private final SupportTicketService service;

    @PostMapping
    public ResponseEntity<SupportTicketResponse> createTicket(
            @Valid @RequestBody SupportTicketRequest request) {
        SupportTicketResponse response = service.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<SupportTicketResponse>> getTickets(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Currency currency,
            @RequestParam(required = false)
            @DecimalMin(value = "0.0", message = "minCost must be greater than or equal to 0")
            BigDecimal minCost,
            @RequestParam(required = false)
            @DecimalMin(value = "0.0", message = "maxCost must be greater than or equal to 0")
            BigDecimal maxCost,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        // Validar que from <= to si ambos están presentes
        if (from != null && to != null && from.isAfter(to)) {
            throw new InvalidDateRangeException("Parameter 'from' must be before or equal to 'to'");
        }

        Page<SupportTicketResponse> tickets = service.getTicketsWithFilters(
                q, status, currency, minCost, maxCost, from, to, pageable
        );

        return ResponseEntity.ok(tickets);
    }
}
