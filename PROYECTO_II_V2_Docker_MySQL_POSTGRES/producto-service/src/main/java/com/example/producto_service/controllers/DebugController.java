package com.example.producto_service.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/auth-info")
    public Map<String, Object> getAuthInfo() {
        Map<String, Object> response = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null) {
            response.put("authenticated", false);
            response.put("message", "No authentication found");
        } else {
            response.put("authenticated", auth.isAuthenticated());
            response.put("principal", auth.getPrincipal());
            response.put("name", auth.getName());
            response.put("authorities", auth.getAuthorities().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()));
            response.put("details", auth.getDetails());
        }
        
        return response;
    }
}
