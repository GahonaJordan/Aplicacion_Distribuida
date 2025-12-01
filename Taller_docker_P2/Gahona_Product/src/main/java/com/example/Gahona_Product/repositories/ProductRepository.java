package com.example.Gahona_Product.repositories;

import com.example.Gahona_Product.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductRepository extends CrudRepository<Product, Long> {

}
