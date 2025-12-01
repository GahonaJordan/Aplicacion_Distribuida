// src/main/java/com/example/Gahona_Product/exceptions/ApiException.java
package com.example.Gahona_Product.exceptions;

public class ApiException extends RuntimeException {
    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
