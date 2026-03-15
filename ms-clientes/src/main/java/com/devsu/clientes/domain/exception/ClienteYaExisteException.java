package com.devsu.clientes.domain.exception;

/**
 * Excepción: ClienteYaExisteException
 *
 * Se lanza cuando se intenta crear un cliente con una identificación
 * que ya existe en el sistema (violación de unicidad).
 *
 * Casos de uso:
 * - Crear cliente con identificación duplicada
 * - Registrar usuario que ya existe en la base de datos
 *
 * @author Devsu
 * @version 1.0
 */
public class ClienteYaExisteException extends ClienteDomainException {

    public ClienteYaExisteException(String identificacion) {
        super("Ya existe un cliente registrado con la identificación: " + identificacion);
    }

    public ClienteYaExisteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
