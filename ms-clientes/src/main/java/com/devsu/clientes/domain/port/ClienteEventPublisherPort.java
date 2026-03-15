package com.devsu.clientes.domain.port;

import com.devsu.clientes.domain.entity.Cliente;

/**
 * Puerto de Salida: ClienteEventPublisherPort
 *
 * Define el contrato para publicar eventos de dominio relacionados con clientes.
 * Esta interfaz permite que la lógica de negocio se comunique de forma asíncrona
 * con otros microservicios a través de mensajería (RabbitMQ en este caso).
 *
 * Implementación: Se proporcionará en la capa de infraestructura, adaptando
 * estos eventos de dominio a mensajes RabbitMQ.
 *
 * Patrón: Event-Driven Architecture (EDA)
 * - Los eventos se publican automáticamente tras cambios de estado en el dominio
 * - Otros microservicios (ms-cuentas) se suscriben a estos eventos
 * - La comunicación es asíncrona y desacoplada
 *
 * @author Devsu
 * @version 1.0
 */
public interface ClienteEventPublisherPort {

    /**
     * Publica un evento indicando que se creó un nuevo cliente.
     * Se ejecuta después de guardar un cliente en la persistencia.
     *
     * Evento: cliente.creado
     * Destinatarios: ms-cuentas y otros servicios interesados
     *
     * @param cliente Cliente recién creado
     * @throws IllegalArgumentException si cliente es nulo
     */
    void publicarClienteCreado(Cliente cliente);

    /**
     * Publica un evento indicando que se actualizaron los datos de un cliente.
     * Se ejecuta después de actualizar información personal del cliente.
     *
     * Evento: cliente.actualizado
     * Destinatarios: ms-cuentas (para sincronizar datos)
     *
     * @param cliente Cliente actualizado
     * @throws IllegalArgumentException si cliente es nulo
     */
    void publicarClienteActualizado(Cliente cliente);

    /**
     * Publica un evento indicando que se habilitó un cliente.
     * Se ejecuta cuando un cliente pasa de INACTIVO/BLOQUEADO a ACTIVO.
     *
     * Evento: cliente.habilitado
     * Destinatarios: ms-cuentas (para reactivar cuentas del cliente)
     *
     * @param clienteId ID del cliente habilitado
     * @param identificacion Identificación del cliente (para cross-reference)
     * @throws IllegalArgumentException si clienteId es nulo o identificacion es nula
     */
    void publicarClienteHabilitado(Long clienteId, String identificacion);

    /**
     * Publica un evento indicando que se inhabilitó un cliente.
     * Se ejecuta cuando un cliente cambia de ACTIVO a INACTIVO.
     *
     * Evento: cliente.inhabilitado
     * Destinatarios: ms-cuentas (para desactivar cuentas del cliente)
     *
     * Caso de uso: El cliente solicita cerrar su cuenta, o el banco inhabilita
     * la cuenta por razones administrativas.
     *
     * @param clienteId ID del cliente inhabilitado
     * @param identificacion Identificación del cliente
     * @throws IllegalArgumentException si clienteId es nulo o identificacion es nula
     */
    void publicarClienteInhabilitado(Long clienteId, String identificacion);

    /**
     * Publica un evento indicando que se bloqueó un cliente.
     * Se ejecuta cuando se bloquea una cuenta (p.ej., por inactividad o seguridad).
     *
     * Evento: cliente.bloqueado
     * Destinatarios: ms-cuentas (para bloquear cuentas del cliente)
     *
     * @param clienteId ID del cliente bloqueado
     * @param identificacion Identificación del cliente
     * @throws IllegalArgumentException si clienteId es nulo o identificacion es nula
     */
    void publicarClienteBloqueado(Long clienteId, String identificacion);

    /**
     * Publica un evento indicando que se desbloqueó un cliente.
     * Se ejecuta cuando se resuelve una situación de seguridad y se desbloquea la cuenta.
     *
     * Evento: cliente.desbloqueado
     * Destinatarios: ms-cuentas (para desbloquear cuentas del cliente)
     *
     * @param clienteId ID del cliente desbloqueado
     * @param identificacion Identificación del cliente
     * @throws IllegalArgumentException si clienteId es nulo o identificacion es nula
     */
    void publicarClienteDesbloqueado(Long clienteId, String identificacion);

    /**
     * Publica un evento indicando que se eliminó un cliente (soft delete).
     * Se ejecuta cuando se marca un cliente como INACTIVO de forma permanente.
     *
     * Evento: cliente.eliminado
     * Destinatarios: ms-cuentas (para marcar cuentas como eliminadas)
     *
     * Nota: Es un soft delete, el registro sigue en la base de datos para auditoría.
     *
     * @param clienteId ID del cliente eliminado
     * @param identificacion Identificación del cliente
     * @throws IllegalArgumentException si clienteId es nulo o identificacion es nula
     */
    void publicarClienteEliminado(Long clienteId, String identificacion);

    /**
     * Publica un evento indicando que se cambió la contraseña del cliente.
     * Se ejecuta después de aprobar un cambio de contraseña en el dominio.
     *
     * Evento: cliente.contraseniaActualizada
     * Destinatarios: Servicios de auditoría y seguridad
     *
     * @param clienteId ID del cliente que cambió contraseña
     * @throws IllegalArgumentException si clienteId es nulo
     */
    void publicarContraseniaActualizada(Long clienteId);
}
