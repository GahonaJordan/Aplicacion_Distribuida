package com.espe.oauth_server.controller;

import com.espe.oauth_server.dto.ApiResponse;
import com.espe.oauth_server.dto.RegisterRequest;
import com.espe.oauth_server.entity.User;
import com.espe.oauth_server.service.UserService;
import com.espe.oauth_server.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost", "http://localhost:3000", "http://127.0.0.1", "http://127.0.0.1:3000"}, allowCredentials = "true")
public class AuthController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            Set<String> roles = user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toSet());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Usuario registrado exitosamente", 
                            new UserResponse(user.getId(), user.getUsername(), user.getEmail(), roles)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage() != null ? e.getMessage() : "Error en el registro"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.authenticate(request.getUsername(), request.getPassword());
            Set<String> roles = user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toSet());
            
            System.out.println("=== AUTH LOGIN ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("User ID: " + user.getId());
            System.out.println("Roles: " + roles);
            System.out.println("User object: " + user);
            System.out.println("User roles object: " + user.getRoles());
            
            // Generar JWT token
            String token = jwtTokenService.generateToken(user);
            System.out.println("Token generated successfully");
            
            // Crear respuesta con token
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("roles", roles);
            response.put("token", token);
            response.put("tokenType", "Bearer");
            
            System.out.println("Login response created");
            System.out.println("==================");
            
            return ResponseEntity.ok(ApiResponse.ok("Login exitoso", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Credenciales inválidas"));
        }
    }

    // DTOs internos
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    static class UserResponse {
        private Long id;
        private String username;
        private String email;
        private Set<String> roles;
    }

    @lombok.Data
    static class LoginRequest {
        private String username;
        private String password;
    }
}
