package com.devsu.cuentas.infrastructure.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para habilitar la propagación de TraceId y SpanId
 * en los mensajes asincrónicos de RabbitMQ usando Micrometer Tracing.
 */
@Configuration
public class RabbitMQObservationConfig {

    /**
     * Habilita la observación (tracing) en el RabbitTemplate, que se encarga 
     * de interceptar los mensajes salientes e inyectar los headers (b3, traceparent) 
     * necesarios para que el consumidor pueda continuar la traza.
     */
    @Bean
    public RabbitTemplateCustomizer rabbitTemplateObservationCustomizer() {
        return rabbitTemplate -> {
            rabbitTemplate.setObservationEnabled(true);
        };
    }

    /**
     * Habilita la observación (tracing) en los Listeners (@RabbitListener), para que 
     * lean los headers inyectados y reanuden el contexto de la traza al momento 
     * de consumirlo.
     */
    @Bean
    public ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer(ObservationRegistry observationRegistry) {
        return container -> {
            container.setObservationEnabled(true);
        };
    }
}
