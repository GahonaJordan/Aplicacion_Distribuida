package com.example.chat_backend.CONFIG;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //Poner el SecurityConfig si se maneja usuarios, caso contrario dessactivarlo de esta forma PD: tambien se puede quitar la dependnecia 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable())  // Desactiva autenticación básica
                .formLogin(form -> form.disable());           // Desactiva formulario de login
        return http.build();
    }
}
