package com.devsu.clientes.domain.exception;

/**
 * Excepción Base de Dominio: ClienteDomainException
 *
 * Clase base para todas las excepciones relacionadas con violaciones
 * de reglas de negocio en el dominio de Clientes.
 *
 * Se extiende RuntimeException para indicar que son excepciones no verificadas,
 * permitiendo que se propaguen sin forzar declaración en firmas de métodos.
 *
 * @author Devsu
 * @version 1.0
 */
public abstract class ClienteDomainException extends RuntimeException {

    public ClienteDomainException(String mensaje) {
        super(mensaje);
    }

    public ClienteDomainException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
