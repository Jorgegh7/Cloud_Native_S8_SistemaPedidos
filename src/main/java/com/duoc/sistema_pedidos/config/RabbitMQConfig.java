package com.duoc.sistema_pedidos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String GUIA_QUEUE = "guiaQueue";
    public static final String GUIA_ERROR_QUEUE = "guiaErrorQueue";
    public static final String GUIA_EXCHANGE = "guiaExchange";
    public static final String ROUTING_KEY_EXITO = "guia.exito";
    public static final String ROUTING_KEY_ERROR = "guia.error";

    @Bean
    public Queue guiaQueue() {
        return new Queue(GUIA_QUEUE, true);
    }

    @Bean
    public Queue guiaErrorQueue() {
        return new Queue(GUIA_ERROR_QUEUE, true);
    }

    @Bean
    public DirectExchange guiaExchange() {
        return new DirectExchange(GUIA_EXCHANGE);
    }

    @Bean
    public Binding bindingExito(Queue guiaQueue, DirectExchange guiaExchange) {
        return BindingBuilder.bind(guiaQueue)
                .to(guiaExchange)
                .with(ROUTING_KEY_EXITO);
    }

    @Bean
    public Binding bindingError(Queue guiaErrorQueue, DirectExchange guiaExchange) {
        return BindingBuilder.bind(guiaErrorQueue)
                .to(guiaExchange)
                .with(ROUTING_KEY_ERROR);
    }
}