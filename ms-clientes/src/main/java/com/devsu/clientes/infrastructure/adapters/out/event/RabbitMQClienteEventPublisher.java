package com.devsu.clientes.infrastructure.adapters.out.event;

import com.devsu.clientes.domain.entity.Cliente;
import com.devsu.clientes.domain.port.ClienteEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQClienteEventPublisher implements ClienteEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "cliente.events";

    @Override
    public void publicarClienteCreado(Cliente cliente) {
        log.info("Publicando evento: Cliente Creado - ID: {}", cliente.getIdentificacion());
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.creado", cliente.getIdentificacion());
    }

    @Override
    public void publicarClienteActualizado(Cliente cliente) {
        log.info("Publicando evento: Cliente Actualizado - ID: {}", cliente.getIdentificacion());
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.actualizado", cliente.getIdentificacion());
    }

    @Override
    public void publicarContraseniaActualizada(Long clienteId) {
        log.info("Publicando evento: Contraseña Actualizada - ID: {}", clienteId);
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.contrasena.actualizada", clienteId);
    }

    @Override
    public void publicarClienteHabilitado(Long clienteId, String identificacion) {
        log.info("Publicando evento: Cliente Habilitado - ID: {}", identificacion);
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.habilitado", identificacion);
    }

    @Override
    public void publicarClienteInhabilitado(Long clienteId, String identificacion) {
        log.info("Publicando evento: Cliente Inhabilitado - ID: {}", identificacion);
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.inhabilitado", identificacion);
    }

    @Override
    public void publicarClienteBloqueado(Long clienteId, String identificacion) {
        log.info("Publicando evento: Cliente Bloqueado - ID: {}", identificacion);
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.bloqueado", identificacion);
    }

    @Override
    public void publicarClienteDesbloqueado(Long clienteId, String identificacion) {
        log.info("Publicando evento: Cliente Desbloqueado - ID: {}", identificacion);
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.desbloquear", identificacion);
    }

    @Override
    public void publicarClienteEliminado(Long clienteId, String identificacion) {
        log.info("Publicando evento: Cliente Eliminado - ID: {}", identificacion);
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.eliminado", identificacion);
    }
}
