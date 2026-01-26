package com.example.ordenes_service.service;

import com.example.ordenes_service.client.InventarioServiceClient;
import com.example.ordenes_service.client.ProductoServiceClient;
import com.example.ordenes_service.client.ProveedorServiceClient;
import com.example.ordenes_service.dto.ActualizarEstadoOrdenDTO;
import com.example.ordenes_service.dto.BodegaValidacionDTO;
import com.example.ordenes_service.dto.DetalleOrdenCompraDTO;
import com.example.ordenes_service.dto.DetalleOrdenCompraResponseDTO;
import com.example.ordenes_service.dto.MovimientoInventarioRequestDTO;
import com.example.ordenes_service.dto.ProductoMovimientoDTO;
import com.example.ordenes_service.dto.ProductoValidacionDTO;
import com.example.ordenes_service.dto.ProveedorValidacionDTO;
import com.example.ordenes_service.dto.TipoMovimiento;
import com.example.ordenes_service.dto.OrdenCompraCreateDTO;
import com.example.ordenes_service.dto.OrdenCompraResponseDTO;
import com.example.ordenes_service.dto.OrdenCompraResumenDTO;
import com.example.ordenes_service.entity.OrdenCompra;
import com.example.ordenes_service.entity.DetalleOrdenCompra;
import com.example.ordenes_service.entity.EstadoOrden;
import com.example.ordenes_service.exception.InventoryServiceException;
import com.example.ordenes_service.exception.InvalidOperationException;
import com.example.ordenes_service.exception.ResourceNotFoundException;
import com.example.ordenes_service.repository.OrdenCompraRepository;
import com.example.ordenes_service.service.OrdenesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdenesServiceImpl implements OrdenesService {

    private final OrdenCompraRepository ordenRepository;
    private final ProductoServiceClient productoClient;
    private final ProveedorServiceClient proveedorClient;
    private final InventarioServiceClient inventarioClient;

    @Override
    @Transactional
    public OrdenCompraResponseDTO crearOrdenCompra(OrdenCompraCreateDTO dto) {
        log.info("Creando orden de compra");

        // 1. Validar proveedor y obtener datos
        proveedorClient.validateSupplier(dto.getProveedorId());
        ProveedorValidacionDTO proveedor = proveedorClient.getSupplier(dto.getProveedorId());

        // 2. Validar bodega y obtener datos
        inventarioClient.validarBodega(dto.getBodegaId());
        BodegaValidacionDTO bodega = inventarioClient.getWarehouse(dto.getBodegaId());

        // 3. Validar productos
        for (DetalleOrdenCompraDTO detalle : dto.getDetalles()) {
            productoClient.validarProducto(detalle.getProductoId());
        }

        // 4. Crear orden
        OrdenCompra orden = new OrdenCompra();
        orden.setNumeroOrden(generarNumeroOrden());
        orden.setProveedorId(dto.getProveedorId());
        orden.setBodegaId(dto.getBodegaId());
        orden.setFechaOrden(LocalDate.now());
        orden.setFechaEsperada(dto.getFechaEsperada());
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setNotas(dto.getNotas());

        // 5. Agregar detalles (calcular subtotal manualmente)
        for (DetalleOrdenCompraDTO detalleDTO : dto.getDetalles()) {
            ProductoValidacionDTO producto = productoClient.getProduct(detalleDTO.getProductoId());

            DetalleOrdenCompra detalle = new DetalleOrdenCompra();
            detalle.setProductoId(detalleDTO.getProductoId());
            detalle.setSku(detalleDTO.getSku());
            detalle.setNombreProducto(producto.getName());
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            // calcular subtotal para evitar nulls antes del persist
            if (detalle.getCantidad() != null && detalle.getPrecioUnitario() != null) {
                detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            } else {
                detalle.setSubtotal(BigDecimal.ZERO);
            }

            orden.agregarDetalle(detalle);
        }

        // 6. Calcular total
        orden.calcularTotal();

        // 7. Guardar
        OrdenCompra guardada = ordenRepository.save(orden);
        log.info("Orden creada: {}", guardada.getNumeroOrden());

        return mapearAResponseDTO(guardada, proveedor.getBusinessName(), bodega.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraResponseDTO> obtenerTodasOrdenes() {
        Iterable<OrdenCompra> it = ordenRepository.findAll();
        return StreamSupport.stream(it.spliterator(), false)
                .map(this::mapearAResponseDTOConNombres)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraResumenDTO> obtenerResumenOrdenes() {
        Iterable<OrdenCompra> it = ordenRepository.findAll();
        return StreamSupport.stream(it.spliterator(), false)
                .map(this::mapearAResumenDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenCompraResponseDTO obtenerOrdenPorId(Long id) {
        OrdenCompra orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + id));
        return mapearAResponseDTOConNombres(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenCompraResponseDTO obtenerOrdenPorNumero(String numeroOrden) {
        OrdenCompra orden = ordenRepository.findByNumeroOrden(numeroOrden)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada: " + numeroOrden));
        return mapearAResponseDTOConNombres(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraResumenDTO> obtenerOrdenesPorEstado(EstadoOrden estado) {
        return ordenRepository.findByEstado(estado)
                .stream()
                .map(this::mapearAResumenDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrdenCompraResponseDTO aprobarOrden(Long id, ActualizarEstadoOrdenDTO dto) {
        OrdenCompra orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + id));

        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new InvalidOperationException("Solo se pueden aprobar órdenes en estado PENDIENTE");
        }

        orden.setEstado(EstadoOrden.APROBADA);
        if (dto.getNotas() != null) {
            orden.setNotas(orden.getNotas() + "\n[APROBACIÓN] " + dto.getNotas());
        }

        OrdenCompra actualizada = ordenRepository.save(orden);
        log.info("Orden aprobada: {}", orden.getNumeroOrden());

        return mapearAResponseDTOConNombres(actualizada);
    }

    @Override
    @Transactional
    public OrdenCompraResponseDTO recibirOrden(Long id, ActualizarEstadoOrdenDTO dto) {
        OrdenCompra orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + id));

        if (orden.getEstado() != EstadoOrden.APROBADA) {
            throw new InvalidOperationException("Solo se pueden recibir órdenes en estado APROBADA");
        }

        // Actualizar estado
        orden.setEstado(EstadoOrden.RECIBIDA);
        orden.setFechaRecepcion(LocalDate.now());
        if (dto.getNotas() != null) {
            orden.setNotas(orden.getNotas() + "\n[RECEPCIÓN] " + dto.getNotas());
        }

        // Preparar movimiento de inventario
        MovimientoInventarioRequestDTO inventarioRequest = new MovimientoInventarioRequestDTO();
        inventarioRequest.setWarehouseId(orden.getBodegaId());
        inventarioRequest.setType(TipoMovimiento.ENTRADA);
        inventarioRequest.setReferenceType("ORDEN_COMPRA");
        inventarioRequest.setReferenceId(orden.getId());
        inventarioRequest.setNotes("Recepción de orden " + orden.getNumeroOrden());

        List<ProductoMovimientoDTO> productos = orden.getDetalles().stream()
                .map(detalle -> {
                    ProductoMovimientoDTO pm = new ProductoMovimientoDTO();
                    pm.setProductId(detalle.getProductoId());
                    pm.setSku(detalle.getSku());
                    pm.setQuantity(detalle.getCantidad());
                    return pm;
                })
                .collect(Collectors.toList());

        inventarioRequest.setProducts(productos);

        // Actualizar inventario (propagar excepción específica para GlobalExceptionHandler)
        try {
            inventarioClient.registrarMovimientoInventario(inventarioRequest);
        } catch (Exception e) {
            log.error("Error al actualizar inventario: {}", e.getMessage());
            // usar la excepción específica con el constructor correcto (statusCode puede ser null)
            throw new InventoryServiceException(null, "No se pudo actualizar el inventario: " + e.getMessage(), e);
        }

        OrdenCompra actualizada = ordenRepository.save(orden);
        log.info("Orden recibida e inventario actualizado: {}", orden.getNumeroOrden());

        return mapearAResponseDTOConNombres(actualizada);
    }

    @Override
    @Transactional
    public OrdenCompraResponseDTO cancelarOrden(Long id, ActualizarEstadoOrdenDTO dto) {
        OrdenCompra orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + id));

        if (orden.getEstado() == EstadoOrden.RECIBIDA) {
            throw new InvalidOperationException("No se puede cancelar una orden ya recibida");
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        if (dto.getNotas() != null) {
            orden.setNotas(orden.getNotas() + "\n[CANCELACIÓN] " + dto.getNotas());
        }

        OrdenCompra actualizada = ordenRepository.save(orden);
        log.info("Orden cancelada: {}", orden.getNumeroOrden());

        return mapearAResponseDTOConNombres(actualizada);
    }

    // Helpers

    private String generarNumeroOrden() {
        long count = ordenRepository.count() + 1;
        return String.format("OC-%06d", count);
    }

    private OrdenCompraResponseDTO mapearAResponseDTOConNombres(OrdenCompra orden) {
        String nombreProveedor = "N/A";
        String nombreBodega = "N/A";

        try {
            ProveedorValidacionDTO prov = proveedorClient.getSupplier(orden.getProveedorId());
            nombreProveedor = prov != null ? prov.getBusinessName() : "N/A";
        } catch (Exception e) {
            log.warn("No se pudo obtener nombre del proveedor: {}", e.getMessage());
        }

        try {
            BodegaValidacionDTO bod = inventarioClient.getWarehouse(orden.getBodegaId());
            nombreBodega = bod != null ? bod.getName() : "N/A";
        } catch (Exception e) {
            log.warn("No se pudo obtener nombre de la bodega: {}", e.getMessage());
        }

        return mapearAResponseDTO(orden, nombreProveedor, nombreBodega);
    }

    private OrdenCompraResponseDTO mapearAResponseDTO(OrdenCompra orden, String nombreProveedor, String nombreBodega) {
        OrdenCompraResponseDTO resp = new OrdenCompraResponseDTO();
        resp.setId(orden.getId());
        resp.setNumeroOrden(orden.getNumeroOrden());
        resp.setProveedorId(orden.getProveedorId());
        resp.setNombreProveedor(nombreProveedor);
        resp.setBodegaId(orden.getBodegaId());
        resp.setNombreBodega(nombreBodega);
        resp.setFechaOrden(orden.getFechaOrden());
        resp.setFechaEsperada(orden.getFechaEsperada());
        resp.setFechaRecepcion(orden.getFechaRecepcion());
        // convertir enum de entity -> dto
        resp.setEstado(com.example.ordenes_service.dto.EstadoOrden.valueOf(orden.getEstado().name()));
        resp.setMontoTotal(orden.getMontoTotal());
        resp.setNotas(orden.getNotas());

        List<DetalleOrdenCompraResponseDTO> detallesDTO = orden.getDetalles().stream().map(detalle -> {
            DetalleOrdenCompraResponseDTO d = new DetalleOrdenCompraResponseDTO();
            d.setId(detalle.getId());
            d.setProductoId(detalle.getProductoId());
            d.setSku(detalle.getSku());
            d.setNombreProducto(detalle.getNombreProducto());
            d.setCantidad(detalle.getCantidad());
            d.setPrecioUnitario(detalle.getPrecioUnitario());
            d.setSubtotal(detalle.getSubtotal());
            return d;
        }).collect(Collectors.toList());

        resp.setDetalles(detallesDTO);
        resp.setFechaCreacion(orden.getFechaCreacion());
        resp.setFechaActualizacion(orden.getFechaActualizacion());
        return resp;
    }

    private OrdenCompraResumenDTO mapearAResumenDTO(OrdenCompra orden) {
        OrdenCompraResumenDTO resumen = new OrdenCompraResumenDTO();
        resumen.setId(orden.getId());
        resumen.setNumeroOrden(orden.getNumeroOrden());

        try {
            ProveedorValidacionDTO prov = proveedorClient.getSupplier(orden.getProveedorId());
            resumen.setNombreProveedor(prov != null ? prov.getBusinessName() : "N/A");
        } catch (Exception e) {
            log.warn("No se pudo obtener nombre del proveedor");
            resumen.setNombreProveedor("N/A");
        }

        resumen.setFechaOrden(orden.getFechaOrden());
        // convertir enum de entity -> dto
        resumen.setEstado(com.example.ordenes_service.dto.EstadoOrden.valueOf(orden.getEstado().name()));
        resumen.setMontoTotal(orden.getMontoTotal());
        resumen.setTotalItems(orden.getDetalles() != null ? orden.getDetalles().size() : 0);
        return resumen;
    }
}
