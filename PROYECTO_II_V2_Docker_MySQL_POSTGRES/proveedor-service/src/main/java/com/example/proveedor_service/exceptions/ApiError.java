package com.example.proveedor_service.exceptions;

import java.util.Map;

public record ApiError(String timestamp, int status, String message, Map<String, String> errors) {}
