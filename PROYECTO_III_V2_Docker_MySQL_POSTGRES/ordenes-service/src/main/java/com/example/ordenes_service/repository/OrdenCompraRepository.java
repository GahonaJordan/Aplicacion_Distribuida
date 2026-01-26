package com.example.ordenes_service.repository;

import com.example.ordenes_service.entity.EstadoOrden;
import com.example.ordenes_service.entity.OrdenCompra;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenCompraRepository extends CrudRepository<OrdenCompra, Long> {
    Optional<OrdenCompra> findByNumeroOrden(String numeroOrden);
    List<OrdenCompra> findByEstado(EstadoOrden estado);
    List<OrdenCompra> findByProveedorId(Long proveedorId);
    List<OrdenCompra> findByBodegaId(Long bodegaId);
}
