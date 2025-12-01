package com.example.Gahona_Product.controllers;

import com.example.Gahona_Product.dto.ProductDto;
import com.example.Gahona_Product.entities.Product;
import com.example.Gahona_Product.exceptions.ResourceNotFoundException;
import com.example.Gahona_Product.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    private ProductDto toDto(Product p) {
        ProductDto d = new ProductDto();
        d.setId(p.getId());
        d.setName(p.getName());
        d.setCategory(p.getCategory());
        d.setDescription(p.getDescription());
        d.setPrice(p.getPrice());
        d.setStock(p.getStock());
        d.setStatus(p.getStatus());
        return d;
    }

    private Product toEntity(ProductDto d) {
        Product p = new Product();
        p.setId(d.getId());
        p.setName(d.getName());
        p.setCategory(d.getCategory());
        p.setDescription(d.getDescription());
        p.setPrice(d.getPrice());
        p.setStock(d.getStock());
        p.setStatus(d.getStatus());
        return p;
    }

    // Listar todos los productos
    @GetMapping
    public ResponseEntity<List<ProductDto>> list() {
        List<ProductDto> list = productService.findAll().stream().map(this::toDto).toList();
        return ResponseEntity.ok(list);
    }

    // Buscar un producto por id
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    // Guardar un producto
    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto) {
        Product product = toEntity(productDto);
        Product savedProduct = productService.save(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(toDto(savedProduct));
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        Product toSave = toEntity(productDto);
        toSave.setId(id);
        Product updatedProduct = productService.save(toSave);
        return ResponseEntity.ok(toDto(updatedProduct));
    }

    // Eliminar un producto por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
