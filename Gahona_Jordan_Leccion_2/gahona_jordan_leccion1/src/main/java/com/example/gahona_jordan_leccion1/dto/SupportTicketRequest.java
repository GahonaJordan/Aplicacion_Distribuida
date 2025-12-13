package com.example.gahona_jordan_leccion1.dto;

import com.example.gahona_jordan_leccion1.entities.Currency;
import com.example.gahona_jordan_leccion1.entities.Priority;
import com.example.gahona_jordan_leccion1.entities.TicketStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SupportTicketRequest {

    @NotBlank(message = "Ticket number is required")
    @Pattern(regexp = "ST-\\d{4}-\\d{6}", message = "Ticket number must follow format: ST-YYYY-NNNNNN")
    private String ticketNumber;

    @NotBlank(message = "Requester name is required")
    @Size(min = 3, max = 100, message = "Requester name must be between 3 and 100 characters")
    private String requesterName;

    @NotNull(message = "Status is required")
    private TicketStatus status;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotBlank(message = "Category is required")
    @Pattern(regexp = "NETWORK|HARDWARE|SOFTWARE", message = "Category must be NETWORK, HARDWARE, or SOFTWARE")
    private String category;

    @NotNull(message = "Estimated cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated cost must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Estimated cost must have max 8 integer digits and 2 decimal digits")
    private BigDecimal estimatedCost;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    // Getters and Setters

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
