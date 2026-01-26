package com.example.producto_service.services;

import com.example.producto_service.dto.ProductoDto;
import com.example.producto_service.entities.Producto;
import com.example.producto_service.exceptions.DuplicateResourceException;
import com.example.producto_service.exceptions.ResourceNotFoundException;
import com.example.producto_service.repositories.ProductoRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoServiceImpl implements ProdcutoService {

    @Autowired
    private ProductoRespository repo;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return (List<Producto>) repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional
    public Producto create(ProductoDto dto) {
        if (repo.existsBySku(dto.getSku())) {
            throw new DuplicateResourceException("SKU ya existe");
        }
        Producto p = mapDtoToEntity(dto);
        return repo.save(p);
    }

    @Override
    @Transactional
    public Producto update(Long id, ProductoDto dto) {
        Producto existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        if (!existing.getSku().equals(dto.getSku()) && repo.existsBySku(dto.getSku())) {
            throw new DuplicateResourceException("SKU ya existe");
        }
        Producto toSave = mapDtoToEntity(dto);
        toSave.setId(id);
        return repo.save(toSave);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        repo.deleteById(id);
    }

    private Producto mapDtoToEntity(ProductoDto dto) {
        Producto p = new Producto();
        p.setName(dto.getName());
        p.setSku(dto.getSku());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStatus(dto.getStatus() == null ? Producto.ProductStatus.INACTIVE : Producto.ProductStatus.valueOf(dto.getStatus().name()));
        return p;
    }
}
