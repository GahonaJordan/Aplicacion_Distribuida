package com.example.backend_ci_soap.ENDPOINT;

import com.example.backend_ci_soap.DTO.ArticuloEntradaDTO;
import com.example.backend_ci_soap.EXCEPTION.ArticuloException;
import com.example.backend_ci_soap.EXCEPTION.SoapServiceException;
import com.example.backend_ci_soap.SERVICE.ArticuloService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.math.BigDecimal;

import com.example.backend_ci_soap.wsdl.InsertarArticuloRequest;
import com.example.backend_ci_soap.wsdl.InsertarArticuloResponse;
import com.example.backend_ci_soap.wsdl.ConsultarArticuloRequest;
import com.example.backend_ci_soap.wsdl.ConsultarArticuloResponse;
import com.example.backend_ci_soap.wsdl.ArticuloType;

@Endpoint
public class ArticuloEndpoint {

    private static final String NAMESPACE_URI = "http://www.example.com/soap/articulos";

    private final ArticuloService articuloService;

    public ArticuloEndpoint(ArticuloService articuloService) {
        this.articuloService = articuloService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "InsertarArticuloRequest")
    @ResponsePayload
    public InsertarArticuloResponse insertarArticulo(@RequestPayload InsertarArticuloRequest request) {
        try {
            var dto = request.getArticulo();

            ArticuloEntradaDTO entrada = new ArticuloEntradaDTO(
                    dto.getCodigo(),
                    dto.getNombre(),
                    dto.getCategoria(),
                    dto.getPrecioCompra(),
                    dto.getPrecioVenta(),
                    dto.getStock(),
                    dto.getStockMinimo(),
                    dto.getProveedor()
            );

            articuloService.crearArticulo(entrada);

            InsertarArticuloResponse response = new InsertarArticuloResponse();
            response.setMensaje("Artículo insertado correctamente: " + dto.getCodigo());
            return response;

        } catch (ArticuloException ex) {
            throw new SoapServiceException("Error al insertar artículo: " + ex.getMessage());
        }
    }

    // Consultar Artículo por código o nombre
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarArticuloRequest")
    @ResponsePayload
    public ConsultarArticuloResponse consultarArticulo(@RequestPayload ConsultarArticuloRequest request) {
        var codigo = request.getCodigo();

        var optional = articuloService.buscarArticuloPorCodigo(codigo);
        ConsultarArticuloResponse response = new ConsultarArticuloResponse();

        if (optional.isPresent()) {
            var a = optional.get();
            var articuloSoap = new ArticuloType();

            articuloSoap.setCodigo(a.getCodigo());
            articuloSoap.setNombre(a.getNombre());
            articuloSoap.setCategoria(a.getCategoria());
            articuloSoap.setPrecioCompra(a.getPrecioCompra());
            articuloSoap.setPrecioVenta(a.getPrecioVenta());
            articuloSoap.setStock(a.getStock());
            articuloSoap.setStockMinimo(a.getStockMinimo());
            articuloSoap.setProveedor(a.getProveedor());

            response.setArticulo(articuloSoap);
        }

        return response;
    }
}
