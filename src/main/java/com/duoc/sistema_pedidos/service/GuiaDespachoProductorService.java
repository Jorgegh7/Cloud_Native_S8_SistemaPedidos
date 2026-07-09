package com.duoc.sistema_pedidos.service;

import com.duoc.sistema_pedidos.config.RabbitMQConfig;
import com.duoc.sistema_pedidos.dto.GuiaDespachoMensaje;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuiaDespachoProductorService {

    private RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void enviarExito(Long guiaId, String numeroGuia) {
        enviar(RabbitMQConfig.GUIA_QUEUE, guiaId, numeroGuia, "OK", "Guia procesada correctamente");
    }

    public void enviarError(Long guiaId, String numeroGuia, String detalleError) {
        enviar(RabbitMQConfig.GUIA_ERROR_QUEUE, guiaId, numeroGuia, "ERROR", detalleError);
    }

    private void enviar(String queueName, Long guiaId, String numeroGuia, String estado, String detalle) {
        try {
            GuiaDespachoMensaje mensaje = new GuiaDespachoMensaje(guiaId, numeroGuia, estado, detalle);
            String json = objectMapper.writeValueAsString(mensaje);
            rabbitTemplate.convertAndSend(queueName, json);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar mensaje a RabbitMQ", e);
        }
    }
}