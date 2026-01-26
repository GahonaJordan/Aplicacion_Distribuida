// java
package com.example.inventario_service.services;

import com.example.inventario_service.dto.BodegaCreateDTO;
import com.example.inventario_service.dto.BodegaResponseDTO;
import com.example.inventario_service.dto.BodegaUpdateDTO;
import com.example.inventario_service.entities.Bodega;
import com.example.inventario_service.exceptions.ResourceNotFoundException;
import com.example.inventario_service.exceptions.InvalidOperationException;
import com.example.inventario_service.repositories.BodegaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class BodegaServiceImpl implements BodegaService {

    private static final Logger log = LoggerFactory.getLogger(BodegaServiceImpl.class);

    private final BodegaRepository bodegaRepository;

    public BodegaServiceImpl(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    @Override
    public BodegaResponseDTO createBodega(BodegaCreateDTO dto) {
        log.info("Creando bodega con código: {}", dto.getCode());

        if (bodegaRepository.existsByCode(dto.getCode())) {
            throw new InvalidOperationException("Ya existe una bodega con el código: " + dto.getCode());
        }

        Bodega b = new Bodega();
        b.setCode(dto.getCode());
        b.setName(dto.getName());
        b.setAddress(dto.getAddress());
        b.setCity(dto.getCity());
        b.setStatus(dto.getStatus());

        Bodega saved = bodegaRepository.save(b);
        log.info("Bodega creada: {}", saved.getCode());
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BodegaResponseDTO> getAllBodegas() {
        List<Bodega> lista = new ArrayList<>();
        bodegaRepository.findAll().forEach(lista::add);
        return lista.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BodegaResponseDTO> getActiveBodegas() {
        return bodegaRepository.findByStatus(Bodega.ProductStatus.ACTIVE)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BodegaResponseDTO getBodegaById(Long id) {
        Bodega b = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));
        return mapToResponseDTO(b);
    }

    @Override
    @Transactional(readOnly = true)
    public BodegaResponseDTO getBodegaByCode(String code) {
        Bodega b = bodegaRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con código: " + code));
        return mapToResponseDTO(b);
    }

    @Override
    public BodegaResponseDTO updateBodega(Long id, BodegaUpdateDTO dto) {
        Bodega b = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));

        b.setName(dto.getName());
        b.setAddress(dto.getAddress());
        b.setCity(dto.getCity());
        b.setStatus(dto.getStatus());

        Bodega updated = bodegaRepository.save(b);
        log.info("Bodega actualizada: {}", updated.getCode());
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteBodega(Long id) {
        if (!bodegaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bodega no encontrada con ID: " + id);
        }
        bodegaRepository.deleteById(id);
        log.info("Bodega eliminada con ID: {}", id);
    }

    private BodegaResponseDTO mapToResponseDTO(Bodega b) {
        BodegaResponseDTO r = new BodegaResponseDTO();
        r.setId(b.getId());
        r.setCode(b.getCode());
        r.setName(b.getName());
        r.setAddress(b.getAddress());
        r.setCity(b.getCity());
        r.setStatus(b.getStatus());
        r.setCreatedAt(b.getCreatedAt());
        return r;
    }
}
