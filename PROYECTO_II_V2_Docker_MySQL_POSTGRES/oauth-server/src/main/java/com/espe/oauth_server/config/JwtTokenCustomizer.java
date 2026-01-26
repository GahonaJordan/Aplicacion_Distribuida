package com.espe.oauth_server.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType().getValue().equals("access_token")) {
            Authentication principal = context.getPrincipal();
            
            // Extraer los roles del usuario (ROLE_ADMIN, ROLE_USER, etc.)
            List<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("ROLE_"))
                    .collect(Collectors.toList());
            
            // Agregar los roles al JWT
            if (!roles.isEmpty()) {
                context.getClaims().claim("roles", roles);
            }
            
            // Agregar el username
            context.getClaims().claim("username", principal.getName());
        }
    }
}
