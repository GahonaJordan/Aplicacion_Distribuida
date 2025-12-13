package com.example.gahona_jordan_leccion1.exception;

public class DuplicateTicketNumberException extends RuntimeException {
    public DuplicateTicketNumberException(String message) {
        super(message);
    }
}
