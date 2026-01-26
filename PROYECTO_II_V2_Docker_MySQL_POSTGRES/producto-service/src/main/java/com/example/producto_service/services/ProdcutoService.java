package com.example.producto_service.services;

import com.example.producto_service.dto.ProductoDto;
import com.example.producto_service.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface ProdcutoService {

    //Listar todos los productos
    List<Producto> findAll();

    //Buscar un producto por id
    Optional<Producto> findById(Long id);

    //Crear un nuevo producto
    Producto create(ProductoDto dto);

    //Actualizar un producto existente
    Producto update(Long id, ProductoDto dto);

    //Eliminar un producto por id
    void deleteById(Long id);
}
