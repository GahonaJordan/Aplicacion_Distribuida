package com.example.backend_ci_soap.REPOSITORY;

import com.example.backend_ci_soap.MODEL.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    //Buscar un articulo por codigo
    Optional<Articulo> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);

    //Buscar un articulo por nombre
    Optional<Articulo> findByNombre(String nombre);
    boolean existsByNombre(String nombre);

    // Buscar por nombre que contenga una cadena (insensible a mayúsculas/minúsculas)
    List<Articulo> findByNombreContainingIgnoreCase(String nombre);
}
