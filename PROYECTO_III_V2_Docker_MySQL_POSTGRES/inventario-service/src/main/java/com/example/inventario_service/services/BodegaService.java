package com.example.inventario_service.services;

import com.example.inventario_service.dto.BodegaCreateDTO;
import com.example.inventario_service.dto.BodegaResponseDTO;
import com.example.inventario_service.dto.BodegaUpdateDTO;
import com.example.inventario_service.entities.Bodega;

import java.util.List;

public interface BodegaService {

    BodegaResponseDTO createBodega(BodegaCreateDTO dto);

    List<BodegaResponseDTO> getAllBodegas();

    List<BodegaResponseDTO> getActiveBodegas();

    BodegaResponseDTO getBodegaById(Long id);

    BodegaResponseDTO getBodegaByCode(String code);

    BodegaResponseDTO updateBodega(Long id, BodegaUpdateDTO dto);

    void deleteBodega(Long id);
}
