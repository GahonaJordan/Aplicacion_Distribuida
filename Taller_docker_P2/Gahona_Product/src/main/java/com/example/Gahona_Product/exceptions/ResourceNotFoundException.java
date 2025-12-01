// src/main/java/com/example/Gahona_Product/exceptions/ResourceNotFoundException.java
package com.example.Gahona_Product.exceptions;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
