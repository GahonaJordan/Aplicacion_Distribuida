package com.example.gahona_jordan_leccion1.specification;

import com.example.gahona_jordan_leccion1.entities.Currency;
import com.example.gahona_jordan_leccion1.entities.SupportTicket;
import com.example.gahona_jordan_leccion1.entities.TicketStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupportTicketSpecification {

    public static Specification<SupportTicket> withFilters(
            String q,
            TicketStatus status,
            Currency currency,
            BigDecimal minCost,
            BigDecimal maxCost,
            LocalDateTime from,
            LocalDateTime to) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro de búsqueda de texto (q)
            if (q != null && !q.trim().isEmpty()) {
                String searchPattern = "%" + q.toLowerCase() + "%";
                Predicate ticketNumberPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("ticketNumber")),
                        searchPattern
                );
                Predicate requesterNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("requesterName")),
                        searchPattern
                );
                predicates.add(criteriaBuilder.or(ticketNumberPredicate, requesterNamePredicate));
            }

            // Filtro de estado
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // Filtro de moneda
            if (currency != null) {
                predicates.add(criteriaBuilder.equal(root.get("currency"), currency));
            }

            // Filtro de costo mínimo
            if (minCost != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("estimatedCost"), minCost));
            }

            // Filtro de costo máximo
            if (maxCost != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("estimatedCost"), maxCost));
            }

            // Filtro de rango de fechas
            if (from != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), from));
            }

            if (to != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), to));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
