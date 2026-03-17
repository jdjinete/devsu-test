package com.devsu.cuentas.infrastructure.adapters.in.event;

import com.devsu.cuentas.application.port.in.CuentaUseCase;
import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteEventConsumer {

    private final CuentaUseCase cuentaUseCase;

    /**
     * Escucha eventos de inactivación/eliminación de clientes (Requerimiento comunicación asíncrona).
     * El mensaje esperado es un Map con clientID o un objeto JSON.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleClienteInhabilitadoEvent(Map<String, Object> event) {
        log.info("Evento de inhabilitación de cliente recibido: {}", event);
        
        try {
            Long clienteId = Long.valueOf(event.get("clienteId").toString());
            
            // Buscar todas las cuentas asociadas al cliente
            List<Cuenta> cuentas = cuentaUseCase.obtenerCuentasPorCliente(clienteId);
            
            // Bloquear/Inactivar cuentas (Requerimiento comunicación asíncrona)
            for (Cuenta cuenta : cuentas) {
                log.info("Inactivando cuenta {} por inhabilitación de cliente {}", cuenta.getNumeroCuenta(), clienteId);
                cuenta.setEstado(false);
                cuentaUseCase.actualizarCuenta(cuenta);
            }
        } catch (Exception e) {
            log.error("Error procesando evento de cliente: {}", e.getMessage());
        }
    }
}
