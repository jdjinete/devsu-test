package com.devsu.clientes.domain.exception;

/**
 * Excepción: ClienteInactivoException
 *
 * Se lanza cuando se intenta realizar una operación en un cliente
 * que está en estado INACTIVO o BLOQUEADO.
 *
 * Casos de uso:
 * - Intentar hacer login con cliente inactivo
 * - Solicitar operación bancaria con cliente bloqueado
 * - Actualizar datos de cliente inhabilitado
 *
 * @author Devsu
 * @version 1.0
 */
public class ClienteInactivoException extends ClienteDomainException {

    public ClienteInactivoException(Long clienteId) {
        super("El cliente con ID " + clienteId + " está inactivo o bloqueado y no puede realizar operaciones");
    }

    public ClienteInactivoException(String identificacion) {
        super("El cliente con identificación " + identificacion + " está inactivo o bloqueado");
    }

    public ClienteInactivoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
