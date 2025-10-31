package com.example.backend_ci_soap.EXCEPTION;

public class ArticuloException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ArticuloException() {
        super();
    }

    public ArticuloException(String message) {
        super(message);
    }

    public ArticuloException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticuloException(Throwable cause) {
        super(cause);
    }
}
