package com.example.Gahona_Product.services;

import com.example.Gahona_Product.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    //Listar todos los productos
    List<Product> findAll();

    //Buscar un producto por id
    Optional<Product> findById(Long id);

    //Guardar un producto y actualizar un producto
    Product save(Product product);

    //Eliminar un producto por id
    void deleteById(Long id);
}
