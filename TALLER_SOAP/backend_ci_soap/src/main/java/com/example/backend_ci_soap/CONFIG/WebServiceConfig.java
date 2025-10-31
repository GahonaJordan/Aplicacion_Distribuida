package com.example.backend_ci_soap.CONFIG;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*"); // ✅ importante que sea /ws/*
    }

    @Bean(name = "articulos")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema articulosSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("ArticuloPort");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace("http://www.example.com/soap/articulos");
        definition.setSchema(articulosSchema);
        return definition;
    }

    @Bean
    public XsdSchema articulosSchema() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/articulos.xsd"));
    }
}
