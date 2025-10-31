package com.example.backend_ci_soap;

import com.example.backend_ci_soap.SERVICE.ArticuloService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
public class TestConfig {

    // XSD mínimo en memoria para evitar dependencia de archivo físico
    private static final String MIN_XSD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n" +
                    "           targetNamespace=\"http://www.example.com/soap/articulos\"\n" +
                    "           xmlns=\"http://www.example.com/soap/articulos\"\n" +
                    "           elementFormDefault=\"qualified\">\n" +
                    "  <xs:element name=\"InsertarArticuloRequest\">\n" +
                    "    <xs:complexType>\n" +
                    "      <xs:sequence>\n" +
                    "        <xs:element name=\"articulo\" minOccurs=\"1\" maxOccurs=\"1\">\n" +
                    "          <xs:complexType>\n" +
                    "            <xs:sequence>\n" +
                    "              <xs:element name=\"codigo\" type=\"xs:string\"/>\n" +
                    "            </xs:sequence>\n" +
                    "          </xs:complexType>\n" +
                    "        </xs:element>\n" +
                    "      </xs:sequence>\n" +
                    "    </xs:complexType>\n" +
                    "  </xs:element>\n" +
                    "</xs:schema>";

    @Bean
    public XsdSchema articulosSchema() {
        return new SimpleXsdSchema(new ByteArrayResource(MIN_XSD.getBytes()));
    }

    @Bean
    public ArticuloService articuloService() {
        return Mockito.mock(ArticuloService.class);
    }
}
