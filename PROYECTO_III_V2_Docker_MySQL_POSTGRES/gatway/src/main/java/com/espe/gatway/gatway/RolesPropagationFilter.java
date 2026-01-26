package com.espe.gatway.gatway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Filtro para propagar roles del JWT a los microservicios downstream
 */
@Component
public class RolesPropagationFilter extends AbstractGatewayFilterFactory<RolesPropagationFilter.Config> {

    public RolesPropagationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    
                    if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                        Jwt jwt = (Jwt) authentication.getPrincipal();
                        
                        // Extraer roles del JWT
                        String roles = authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","));
                        
                        // Extraer username del JWT
                        String username = jwt.getClaimAsString("sub");
                        
                        // Agregar headers personalizados con la información del usuario
                        exchange.getRequest().mutate()
                                .header("X-User-Roles", roles)
                                .header("X-User-Name", username)
                                .build();
                    }
                    
                    return exchange;
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    public static class Config {
        // Configuración si se necesita
    }
}
