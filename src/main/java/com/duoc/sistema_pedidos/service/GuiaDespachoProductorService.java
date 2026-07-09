package com.duoc.sistema_pedidos.service;

import com.duoc.sistema_pedidos.config.RabbitMQConfig;
import com.duoc.sistema_pedidos.dto.GuiaDespachoMensaje;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuiaDespachoProductorService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void enviarExito(Long guiaId, String numeroGuia) {
        enviar(RabbitMQConfig.ROUTING_KEY_EXITO, guiaId, numeroGuia, "OK", "Guia procesada correctamente");
    }

    public void enviarError(Long guiaId, String numeroGuia, String detalleError) {
        enviar(RabbitMQConfig.ROUTING_KEY_ERROR, guiaId, numeroGuia, "ERROR", detalleError);
    }

    private void enviar(String routingKey, Long guiaId, String numeroGuia, String estado, String detalle) {
        try {
            GuiaDespachoMensaje mensaje = new GuiaDespachoMensaje(guiaId, numeroGuia, estado, detalle);
            String json = objectMapper.writeValueAsString(mensaje);
            rabbitTemplate.convertAndSend(RabbitMQConfig.GUIA_EXCHANGE, routingKey, json);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar mensaje a RabbitMQ", e);
        }
    }
}