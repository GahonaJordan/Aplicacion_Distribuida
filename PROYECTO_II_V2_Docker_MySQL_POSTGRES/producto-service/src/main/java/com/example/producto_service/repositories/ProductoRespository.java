package com.example.producto_service.repositories;

import com.example.producto_service.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRespository extends CrudRepository<Producto, Long> {
    boolean existsBySku(String sku);
    Optional<Producto> findBySku(String sku);
}
