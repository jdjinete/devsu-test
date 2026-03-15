package com.devsu.clientes.domain.exception;

/**
 * Excepción: ClienteNoEncontradoException
 *
 * Se lanza cuando se intenta realizar una operación sobre un cliente
 * que no existe en el sistema (búsqueda fallida).
 *
 * Casos de uso:
 * - Obtener cliente por ID y no existe
 * - Obtener cliente por identificación y no existe
 * - Intentar actualizar cliente que no existe
 * - Intentar eliminar cliente que no existe
 *
 * @author Devsu
 * @version 1.0
 */
public class ClienteNoEncontradoException extends ClienteDomainException {

    public ClienteNoEncontradoException(Long clienteId) {
        super("Cliente no encontrado con ID: " + clienteId);
    }

    public ClienteNoEncontradoException(String identificacion) {
        super("Cliente no encontrado con identificación: " + identificacion);
    }

    public ClienteNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
