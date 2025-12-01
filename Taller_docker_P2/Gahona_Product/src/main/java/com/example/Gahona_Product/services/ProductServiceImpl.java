package com.example.Gahona_Product.services;

import com.example.Gahona_Product.entities.Product;
import com.example.Gahona_Product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    //Inyectar el repositorio
    @Autowired
    private ProductRepository repository;

    //Listar todos los productos
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();
    }

    //Buscar un producto por id
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    //Para este caso save y update hacen lo mismo
    @Override
    @Transactional(readOnly = false)
    public Product save(Product product) {
        return repository.save(product);
    }

    //Eliminar un producto por id
    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
