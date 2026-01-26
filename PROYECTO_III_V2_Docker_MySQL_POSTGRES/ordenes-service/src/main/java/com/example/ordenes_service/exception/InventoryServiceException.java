// java
package com.example.ordenes_service.exception;

public class InventoryServiceException extends ExternalServiceException {
    private static final String SERVICE = "Inventory Service";

    public InventoryServiceException(String message) {
        super(SERVICE, null, message);
    }

    public InventoryServiceException(Integer statusCode, String message) {
        super(SERVICE, statusCode, message);
    }

    public InventoryServiceException(Integer statusCode, String message, Throwable cause) {
        super(SERVICE, statusCode, message, cause);
    }
}
