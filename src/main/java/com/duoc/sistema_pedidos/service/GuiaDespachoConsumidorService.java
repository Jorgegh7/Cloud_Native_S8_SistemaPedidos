package com.duoc.sistema_pedidos.service;

import com.duoc.sistema_pedidos.config.RabbitMQConfig;
import com.duoc.sistema_pedidos.dto.GuiaDespachoMensaje;
import com.duoc.sistema_pedidos.model.GuiaProcesada;
import com.duoc.sistema_pedidos.repository.GuiaProcesadaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuiaDespachoConsumidorService {

    private final RabbitTemplate rabbitTemplate;
    private final GuiaProcesadaRepository guiaProcesadaRepository;
    private final ObjectMapper objectMapper;

    public String consumirYGuardar() {
        String message = (String) rabbitTemplate.receiveAndConvert(RabbitMQConfig.GUIA_QUEUE);

        if (message == null) {
            return "No hay mensajes pendientes en guiaQueue";
        }

        try {
            GuiaDespachoMensaje mensaje = objectMapper.readValue(message, GuiaDespachoMensaje.class);

            GuiaProcesada guiaProcesada = new GuiaProcesada();
            guiaProcesada.setGuiaId(mensaje.guiaId());
            guiaProcesada.setNumeroGuia(mensaje.numeroGuia());
            guiaProcesada.setEstado(mensaje.estado());
            guiaProcesada.setDetalle(mensaje.detalle());

            guiaProcesadaRepository.save(guiaProcesada);

            return "Guia #" + mensaje.guiaId() + " guardada en guias_procesadas";
        } catch (Exception e) {
            return "Error al procesar mensaje: " + e.getMessage();
        }
    }
}