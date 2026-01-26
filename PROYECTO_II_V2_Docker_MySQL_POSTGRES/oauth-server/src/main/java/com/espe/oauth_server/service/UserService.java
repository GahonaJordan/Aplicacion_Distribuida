package com.espe.oauth_server.service;

import com.espe.oauth_server.dto.RegisterRequest;
import com.espe.oauth_server.dto.UpdateRoleRequest;
import com.espe.oauth_server.dto.UserResponse;
import com.espe.oauth_server.entity.Role;
import com.espe.oauth_server.entity.User;
import com.espe.oauth_server.repository.RoleRepository;
import com.espe.oauth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
        try {
            System.out.println("=== UserService.registerUser START ===");
            System.out.println("Request: " + request);
            
            // Validaciones
            if (userRepository.existsByUsername(request.getUsername())) {
                System.err.println("ERROR: Usuario ya existe");
                throw new RuntimeException("El usuario ya existe");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                System.err.println("ERROR: Email ya registrado");
                throw new RuntimeException("El email ya está registrado");
            }
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                System.err.println("ERROR: Contraseñas no coinciden");
                throw new RuntimeException("Las contraseñas no coinciden");
            }
            if (request.getPassword().length() < 6) {
                System.err.println("ERROR: Contraseña muy corta");
                throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
            }

            System.out.println("Validaciones OK, buscando rol ROLE_USER...");
            // Asignar rol USER por defecto
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
            
            System.out.println("Rol encontrado: " + userRole.getName());

            System.out.println("Creando usuario...");
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
            
            // Configurar roles después del build para evitar problemas con @Builder.Default
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);

            System.out.println("Guardando usuario en BD...");
            User savedUser = userRepository.save(user);
            System.out.println("Usuario guardado exitosamente con ID: " + savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            System.err.println("=== ERROR EN registerUser ===");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        return user;
    }

    // ===== MÉTODOS PARA GESTIÓN DE USUARIOS =====

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUserRoles(Long userId, UpdateRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener los roles desde la base de datos
        Set<Role> newRoles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));
            newRoles.add(role);
        }

        // Actualizar los roles del usuario
        user.setRoles(newRoles);
        User updatedUser = userRepository.save(user);

        return convertToUserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // No permitir eliminar el usuario admin principal
        if ("admin".equals(user.getUsername())) {
            throw new RuntimeException("No se puede eliminar el usuario administrador principal");
        }
        
        userRepository.delete(user);
    }

    // java
    private UserResponse convertToUserResponse(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.getEnabled()) // <-- usar getEnabled() en lugar de isEnabled()
                .roles(roleNames)
                .build();
    }

}
