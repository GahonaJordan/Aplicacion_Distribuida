package com.example.ordenes_service.exception;

public class ExternalServiceException extends RuntimeException {

    private final String serviceName;
    private final Integer statusCode;

    public ExternalServiceException(String serviceName, String message) {
        this(serviceName, null, message, null);
    }

    public ExternalServiceException(String serviceName, Integer statusCode, String message) {
        this(serviceName, statusCode, message, null);
    }

    public ExternalServiceException(String serviceName, Integer statusCode, String message, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
        this.statusCode = statusCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
