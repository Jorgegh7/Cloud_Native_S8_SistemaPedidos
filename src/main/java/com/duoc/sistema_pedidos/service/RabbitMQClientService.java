package com.duoc.sistema_pedidos.service;

import com.duoc.sistema_pedidos.dto.GuiaDespachoMensaje;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RabbitMQClientService {

    @Value("${rabbitmq.ms.url}")
    private String rabbitMsUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarExito(Long guiaId, String numeroGuia) {
        GuiaDespachoMensaje mensaje = new GuiaDespachoMensaje(guiaId, numeroGuia, "OK", "Guia procesada correctamente");
        restTemplate.postForObject(rabbitMsUrl + "/producir/exito", mensaje, String.class);
    }

    public void enviarError(Long guiaId, String numeroGuia, String detalleError) {
        GuiaDespachoMensaje mensaje = new GuiaDespachoMensaje(guiaId, numeroGuia, "ERROR", detalleError);
        restTemplate.postForObject(rabbitMsUrl + "/producir/error", mensaje, String.class);
    }
}