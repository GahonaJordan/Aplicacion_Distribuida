// language: java
package com.example.ordenes_service.client;

import com.example.ordenes_service.dto.BodegaValidacionDTO;
import com.example.ordenes_service.dto.MovimientoInventarioRequestDTO;
import com.example.ordenes_service.dto.MovimientoInventarioSimpleDTO;
import com.example.ordenes_service.dto.ProductoMovimientoDTO;
import com.example.ordenes_service.exception.InventoryServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventarioServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.inventory.url}")
    private String inventoryServiceUrl; // p.e. http://localhost:9092

    private String baseUrl() {
        return inventoryServiceUrl != null ? inventoryServiceUrl : "";
    }

    public BodegaValidacionDTO getWarehouse(Long bodegaId) {
        String url = String.format("%s/api/bodegas/%d", baseUrl(), bodegaId);
        log.info("Consultando bodega en: {}", url);
        try {
            BodegaValidacionDTO dto = restTemplate.getForObject(url, BodegaValidacionDTO.class);
            if (dto == null) {
                log.error("Bodega {} retornó null", bodegaId);
                throw new InventoryServiceException("Bodega retornó datos inválidos para ID: " + bodegaId);
            }
            return dto;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Bodega no encontrada: {}", bodegaId);
            throw new InventoryServiceException(e.getStatusCode().value(), "Bodega no encontrada con ID: " + bodegaId);
        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al consultar bodega: {} - {}", bodegaId, e.getStatusCode());
            throw new InventoryServiceException(e.getStatusCode().value(), "Error al consultar Inventory Service: " + e.getMessage(), e);
        } catch (RestClientException e) {
            log.error("Error de conexión al consultar bodega: {}", e.getMessage());
            throw new InventoryServiceException(null, "Error al comunicarse con Inventory Service: " + e.getMessage(), e);
        }
    }

    public void validarBodega(Long bodegaId) {
        BodegaValidacionDTO bodega = getWarehouse(bodegaId);
        if (!Boolean.TRUE.equals(bodega.getActive())) {
            throw new InventoryServiceException("La bodega con ID " + bodegaId + " está inactiva");
        }
    }

    public void registrarMovimientoInventario(MovimientoInventarioRequestDTO request) {
        if (request == null || request.getWarehouseId() == null) {
            throw new IllegalArgumentException("Movimiento debe incluir warehouseId");
        }

        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Movimiento debe incluir al menos un producto");
        }

        String url = String.format("%s/api/inventarios/bodega/%d/movimientos", baseUrl(), request.getWarehouseId());
        log.info("Registrando movimiento(s) en Inventory Service: {}", url);

        try {
            // Enviar un POST por cada producto usando la estructura plana que el Inventory Service valida.
            for (ProductoMovimientoDTO producto : request.getProducts()) {
                MovimientoInventarioSimpleDTO simple = new MovimientoInventarioSimpleDTO();
                simple.setProductId(producto.getProductId());
                simple.setSku(producto.getSku());
                simple.setQuantity(producto.getQuantity());
                simple.setType(request.getType());
                simple.setReferenceType(request.getReferenceType());
                simple.setReferenceId(request.getReferenceId());
                simple.setNotes(request.getNotes());

                log.debug("Enviando movimiento para productId={} sku={} qty={}", producto.getProductId(), producto.getSku(), producto.getQuantity());
                ResponseEntity<Void> resp = restTemplate.postForEntity(url, simple, Void.class);
                if (!resp.getStatusCode().is2xxSuccessful()) {
                    log.error("Respuesta inesperada al registrar movimiento: {}", resp.getStatusCode());
                    throw new InventoryServiceException(resp.getStatusCodeValue(),
                            "Respuesta inesperada del Inventory Service: " + resp.getStatusCode());
                }
            }
        } catch (HttpClientErrorException ex) {
            log.error("Error HTTP al registrar movimiento: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            Integer status = ex.getStatusCode() != null ? ex.getStatusCode().value() : null;
            throw new InventoryServiceException(status,
                    "Error al registrar movimiento en Inventory Service: " + ex.getResponseBodyAsString(), ex);
        } catch (RestClientException ex) {
            log.error("Error de conexión al registrar movimiento: {}", ex.getMessage());
            throw new InventoryServiceException(null,
                    "Error de conexión con Inventory Service: " + ex.getMessage(), ex);
        }
    }
}
