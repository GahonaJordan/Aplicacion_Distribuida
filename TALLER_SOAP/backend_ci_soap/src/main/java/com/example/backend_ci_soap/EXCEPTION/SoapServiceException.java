package com.example.backend_ci_soap.EXCEPTION;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT)
public class SoapServiceException extends RuntimeException{
    public SoapServiceException(String message) {
        super(message);
    }
}
