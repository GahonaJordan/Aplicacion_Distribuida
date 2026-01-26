package com.example.ordenes_service.service;

import com.example.ordenes_service.dto.*;
import com.example.ordenes_service.entity.EstadoOrden;

import java.util.List;

public interface OrdenesService {
    OrdenCompraResponseDTO crearOrdenCompra(OrdenCompraCreateDTO dto);

    List<OrdenCompraResponseDTO> obtenerTodasOrdenes();

    List<OrdenCompraResumenDTO> obtenerResumenOrdenes();

    OrdenCompraResponseDTO obtenerOrdenPorId(Long id);

    OrdenCompraResponseDTO obtenerOrdenPorNumero(String numeroOrden);

    List<OrdenCompraResumenDTO> obtenerOrdenesPorEstado(EstadoOrden estado);

    OrdenCompraResponseDTO aprobarOrden(Long id, ActualizarEstadoOrdenDTO dto);

    OrdenCompraResponseDTO recibirOrden(Long id, ActualizarEstadoOrdenDTO dto);

    OrdenCompraResponseDTO cancelarOrden(Long id, ActualizarEstadoOrdenDTO dto);
}
