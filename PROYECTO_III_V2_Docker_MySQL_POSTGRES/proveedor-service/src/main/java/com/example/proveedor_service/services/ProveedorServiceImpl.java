package com.example.proveedor_service.services;

import com.example.proveedor_service.dto.ProveedorDto;
import com.example.proveedor_service.entities.Proveedor;
import com.example.proveedor_service.exceptions.DuplicateResourceException;
import com.example.proveedor_service.exceptions.ResourceNotFoundException;
import com.example.proveedor_service.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> findAll() {
        return (List<Proveedor>) repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional
    public Proveedor create(ProveedorDto dto) {
        if (repo.existsByTaxId(dto.getTaxId())) {
            throw new DuplicateResourceException("TaxId ya existe");
        }
        if (repo.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email ya existe");
        }
        Proveedor p = mapDtoToEntity(dto);
        return repo.save(p);
    }

    @Override
    @Transactional
    public Proveedor update(Long id, ProveedorDto dto) {
        Proveedor existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
        if (!existing.getTaxId().equals(dto.getTaxId()) && repo.existsByTaxId(dto.getTaxId())) {
            throw new DuplicateResourceException("TaxId ya existe");
        }
        if (!existing.getEmail().equals(dto.getEmail()) && repo.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email ya existe");
        }
        Proveedor toSave = mapDtoToEntity(dto);
        toSave.setId(id);
        return repo.save(toSave);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor no encontrado");
        }
        repo.deleteById(id);
    }

    private Proveedor mapDtoToEntity(ProveedorDto dto) {
        Proveedor p = new Proveedor();
        p.setName(dto.getName());
        p.setTaxId(dto.getTaxId());
        p.setEmail(dto.getEmail());
        p.setPhone(dto.getPhone());
        p.setAddress(dto.getAddress());
        p.setStatus(dto.getStatus());
        return p;
    }
}
