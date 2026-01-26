package com.example.ordenes_service.exception;

public class SupplierServiceException extends ExternalServiceException {
    private static final String SERVICE = "Supplier Service";

    public SupplierServiceException(String message) {
        super(SERVICE, null, message);
    }

    public SupplierServiceException(Integer statusCode, String message) {
        super(SERVICE, statusCode, message);
    }

    public SupplierServiceException(Integer statusCode, String message, Throwable cause) {
        super(SERVICE, statusCode, message, cause);
    }
}
