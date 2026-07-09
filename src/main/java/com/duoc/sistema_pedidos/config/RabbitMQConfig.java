package com.duoc.sistema_pedidos.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String GUIA_QUEUE = "guiaQueue";
    public static final String GUIA_ERROR_QUEUE = "guiaErrorQueue";

    @Bean
    public Queue guiaQueue() {
        return new Queue(GUIA_QUEUE, true);
    }

    @Bean
    public Queue guiaErrorQueue() {
        return new Queue(GUIA_ERROR_QUEUE, true);
    }
}