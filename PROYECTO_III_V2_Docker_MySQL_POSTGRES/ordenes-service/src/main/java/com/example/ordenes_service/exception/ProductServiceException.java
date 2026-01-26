package com.example.ordenes_service.exception;

public class ProductServiceException extends ExternalServiceException {
    private static final String SERVICE = "Product Service";

    public ProductServiceException(String message) {
        super(SERVICE, null, message);
    }

    public ProductServiceException(Integer statusCode, String message) {
        super(SERVICE, statusCode, message);
    }

    public ProductServiceException(Integer statusCode, String message, Throwable cause) {
        super(SERVICE, statusCode, message, cause);
    }
}
