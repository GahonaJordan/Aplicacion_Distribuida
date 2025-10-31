package com.example.backend_ci_soap.SERVICE;

import com.example.backend_ci_soap.DTO.ArticuloEntradaDTO;
import com.example.backend_ci_soap.DTO.ArticuloSalidaDTO;
import com.example.backend_ci_soap.EXCEPTION.ArticuloException;
import com.example.backend_ci_soap.MODEL.Articulo;
import com.example.backend_ci_soap.REPOSITORY.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticuloService {

    private ArticuloRepository articuloRepository;

    @Autowired
    public ArticuloService(ArticuloRepository articuloRepository) {
        this.articuloRepository = articuloRepository;
    }

    //Listar todos los articulos
    public List<ArticuloSalidaDTO> listarArticulos() {
        return articuloRepository.findAll()
                .stream()
                .map(a -> new ArticuloSalidaDTO(
                        a.getId(),
                        a.getCodigo(),
                        a.getNombre(),
                        a.getCategoria(),
                        a.getPrecioCompra(),
                        a.getPrecioVenta(),
                        a.getStock(),
                        a.getStockMinimo(),
                        a.getProveedor()
                ))
                .collect(Collectors.toList());
    }

    // Buscar un articulo por codigo
    public Optional<ArticuloSalidaDTO> buscarArticuloPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return Optional.empty();
        }
        return articuloRepository.findByCodigo(codigo)
                .map(a -> new ArticuloSalidaDTO(
                        a.getId(),
                        a.getCodigo(),
                        a.getNombre(),
                        a.getCategoria(),
                        a.getPrecioCompra(),
                        a.getPrecioVenta(),
                        a.getStock(),
                        a.getStockMinimo(),
                        a.getProveedor()
                ));
    }

    // Crear un nuevo articulo
    @Transactional
    public ArticuloSalidaDTO crearArticulo(ArticuloEntradaDTO edto) {
        if (edto == null) {
            throw new IllegalArgumentException("Datos del artículo no pueden ser nulos");
        }
        if (edto.getCodigo() == null || edto.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio");
        }
        if (edto.getPrecioCompra() == null || edto.getPrecioVenta() == null) {
            throw new IllegalArgumentException("Los precios son obligatorios");
        }
        synchronized (this) {
            if (articuloRepository.existsByCodigo(edto.getCodigo())) {
                throw new ArticuloException("Código de artículo ya existe: " + edto.getCodigo());
            }
        }
        if (edto.getPrecioVenta().compareTo(edto.getPrecioCompra()) <= 0) {
            throw new ArticuloException("El precio de venta debe ser mayor al precio de compra");
        }

        Articulo nuevoArticulo = new Articulo(
                null,
                edto.getCodigo(),
                edto.getNombre(),
                edto.getCategoria(),
                edto.getPrecioCompra(),
                edto.getPrecioVenta(),
                edto.getStock(),
                edto.getStockMinimo(),
                edto.getProveedor()
        );

        try {
            Articulo guardado = articuloRepository.save(nuevoArticulo);
            return new ArticuloSalidaDTO(
                    guardado.getId(),
                    guardado.getCodigo(),
                    guardado.getNombre(),
                    guardado.getCategoria(),
                    guardado.getPrecioCompra(),
                    guardado.getPrecioVenta(),
                    guardado.getStock(),
                    guardado.getStockMinimo(),
                    guardado.getProveedor()
            );
        } catch (DataIntegrityViolationException ex) {
            // Manejar posible condición de carrera o violación de constraint
            throw new IllegalStateException("No se pudo crear el artículo (clave duplicada o integridad), código: " + edto.getCodigo(), ex);
        }
    }

    // Actualizar un articulo existente por codigo
    @Transactional
    public Optional<ArticuloSalidaDTO> actualizarArticulo(String codigo, ArticuloEntradaDTO edto) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio");
        }
        return articuloRepository.findByCodigo(codigo).map(articulo -> {
            // Prepare nuevos valores para validar precios
            java.math.BigDecimal nuevoPrecioCompra = edto.getPrecioCompra() != null ? edto.getPrecioCompra() : articulo.getPrecioCompra();
            java.math.BigDecimal nuevoPrecioVenta = edto.getPrecioVenta() != null ? edto.getPrecioVenta() : articulo.getPrecioVenta();

            if (edto.getNombre() != null) {
                articulo.setNombre(edto.getNombre());
            }
            if (edto.getCategoria() != null) {
                articulo.setCategoria(edto.getCategoria());
            }
            if (edto.getPrecioCompra() != null) {
                articulo.setPrecioCompra(edto.getPrecioCompra());
            }
            if (edto.getPrecioVenta() != null) {
                if (edto.getPrecioCompra() != null &&
                        edto.getPrecioVenta().compareTo(edto.getPrecioCompra()) <= 0) {
                    throw new ArticuloException("El precio de venta debe ser mayor al precio de compra");
                }
                articulo.setPrecioVenta(edto.getPrecioVenta());
            }
            if (edto.getStock() != null && edto.getStock() >= 0) {
                articulo.setStock(edto.getStock());
            }
            if (edto.getStockMinimo() != null && edto.getStockMinimo() >= 0) {
                articulo.setStockMinimo(edto.getStockMinimo());
            }
            if (edto.getProveedor() != null) {
                articulo.setProveedor(edto.getProveedor());
            }

            try {
                Articulo actualizado = articuloRepository.save(articulo);

                return new ArticuloSalidaDTO(
                        actualizado.getId(),
                        actualizado.getCodigo(),
                        actualizado.getNombre(),
                        actualizado.getCategoria(),
                        actualizado.getPrecioCompra(),
                        actualizado.getPrecioVenta(),
                        actualizado.getStock(),
                        actualizado.getStockMinimo(),
                        actualizado.getProveedor()
                );
            } catch (DataIntegrityViolationException ex) {
                throw new IllegalStateException("No se pudo actualizar el artículo (clave duplicada o integridad), código: " + codigo, ex);
            }
        });
    }

    // Eliminar un articulo por codigo
    @Transactional
    public boolean eliminarArticuloPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio");
        }
        return articuloRepository.findByCodigo(codigo).map(articulo -> {
            articuloRepository.delete(articulo);
            return true;
        }).orElseThrow(() -> new java.util.NoSuchElementException("Artículo con código " + codigo + " no existe"));
    }
}
