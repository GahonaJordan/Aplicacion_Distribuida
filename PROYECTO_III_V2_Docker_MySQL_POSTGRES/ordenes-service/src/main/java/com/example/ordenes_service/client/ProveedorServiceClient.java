// java
package com.example.ordenes_service.client;

import com.example.ordenes_service.dto.ProveedorValidacionDTO;
import com.example.ordenes_service.exception.SupplierServiceException;
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
public class ProveedorServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.supplier.url}")
    private String supplierServiceUrl; // p.e. http://localhost:9091

    private String baseUrl() {
        return supplierServiceUrl != null ? supplierServiceUrl : "";
    }

    public ProveedorValidacionDTO getSupplier(Long proveedorId) {
        String url = String.format("%s/api/proveedores/%d", baseUrl(), proveedorId);
        log.info("Consultando proveedor en: {}", url);
        try {
            ResponseEntity<ProveedorValidacionDTO> resp = restTemplate.getForEntity(url, ProveedorValidacionDTO.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                return resp.getBody();
            }
            Integer status = resp.getStatusCodeValue();
            log.error("Respuesta inesperada del Supplier Service: {} para proveedor {}", status, proveedorId);
            throw new SupplierServiceException(status, "Respuesta inesperada del Supplier Service: " + resp.getStatusCode());
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Proveedor no encontrado: {}", proveedorId);
            throw new SupplierServiceException(404, "Proveedor no encontrado con ID: " + proveedorId);
        } catch (HttpClientErrorException e) {
            Integer status = e.getStatusCode() != null ? e.getStatusCode().value() : null;
            log.error("Error HTTP al consultar proveedor: {} - {}", proveedorId, e.getStatusCode());
            throw new SupplierServiceException(status, "Error al consultar Supplier Service: " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            log.error("Error de conexión al consultar proveedor: {}", e.getMessage());
            throw new SupplierServiceException(null, "Error al comunicarse con Supplier Service: " + e.getMessage(), e);
        }
    }

    public void validateSupplier(Long proveedorId) {
        ProveedorValidacionDTO proveedor = getSupplier(proveedorId);
        if (!Boolean.TRUE.equals(proveedor.getActive())) {
            throw new SupplierServiceException("El proveedor con ID " + proveedorId + " está inactivo");
        }
    }
}
