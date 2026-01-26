package com.espe.oauth_server.service;

import com.espe.oauth_server.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    
    private final JwtEncoder jwtEncoder;

    public String generateToken(User user) {
        Instant now = Instant.now();
        long expiry = 3600L; // 1 hora en segundos

        // Extraer roles del usuario
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        System.out.println("=== JwtTokenService.generateToken ===");
        System.out.println("User: " + user.getUsername());
        System.out.println("User roles: " + user.getRoles());
        System.out.println("Extracted roles: " + roles);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://oauth-server:9000")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getUsername())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .claim("user_id", user.getId())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        System.out.println("Token generated: " + token.substring(0, Math.min(50, token.length())) + "...");
        System.out.println("=====================================");
        
        return token;
    }
}
