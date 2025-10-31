// java
package com.example.backend_ci_soap.Pruebas_integracion;

import com.example.backend_ci_soap.CONFIG.WebServiceConfig;
import com.example.backend_ci_soap.DTO.ArticuloEntradaDTO;
import com.example.backend_ci_soap.DTO.ArticuloSalidaDTO;
import com.example.backend_ci_soap.SERVICE.ArticuloService;
import com.example.backend_ci_soap.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebServiceConfig.class, com.example.backend_ci_soap.ENDPOINT.ArticuloEndpoint.class, TestConfig.class})
public class Test_Articulo_Integrativa {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ArticuloService articuloService; // mock provisto en TestConfig

    private MockWebServiceClient client;

    @BeforeEach
    void setup() {
        client = MockWebServiceClient.createClient(context);
        Mockito.reset(articuloService);
    }

    @Test
    void insertarArticulo_returnsSuccessMessage() throws Exception {
        String request =
                "<InsertarArticuloRequest xmlns=\"http://www.example.com/soap/articulos\">" +
                        "<articulo>" +
                        "<codigo>C-100</codigo>" +
                        "<nombre>Prueba</nombre>" +
                        "<categoria>Cat</categoria>" +
                        "<precioCompra>10.00</precioCompra>" +
                        "<precioVenta>15.00</precioVenta>" +
                        "<stock>5</stock>" +
                        "<stockMinimo>1</stockMinimo>" +
                        "<proveedor>Prov</proveedor>" +
                        "</articulo>" +
                        "</InsertarArticuloRequest>";

        ArticuloSalidaDTO salida = new ArticuloSalidaDTO();
        salida.setCodigo("C-100");
        Mockito.when(articuloService.crearArticulo(Mockito.any(ArticuloEntradaDTO.class)))
                .thenReturn(salida);

        client.sendRequest(withPayload(new StringSource(request)))
                .andExpect(noFault())
                .andExpect(xpath("//*[local-name()='mensaje']").exists())
                .andExpect(xpath("//*[local-name()='mensaje']").evaluatesTo("Artículo insertado correctamente: C-100"));
    }
}
