package com.devsu.clientes.domain.exception;

/**
 * Excepción: IdentificacionInvalidaException
 *
 * Se lanza cuando la identificación (documento de identidad) no cumple
 * con los requisitos de validación del dominio.
 *
 * Requisitos de identificación:
 * - No puede ser nula ni vacía
 * - debe tener entre 1 y 20 caracteres
 * - Debe ser única en el sistema
 *
 * Casos de uso:
 * - Crear cliente sin identificación
 * - Crear cliente con identificación vacía
 * - Identificación con longitud inválida
 *
 * @author Devsu
 * @version 1.0
 */
public class IdentificacionInvalidaException extends ClienteDomainException {

    public IdentificacionInvalidaException(String mensaje) {
        super(mensaje);
    }

    public IdentificacionInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Factory method para crear excepción cuando identificación es nula/vacía
     */
    public static IdentificacionInvalidaException identificacionVacia() {
        return new IdentificacionInvalidaException("La identificación no puede estar vacía");
    }

    /**
     * Factory method para crear excepción cuando identificación es muy corta
     */
    public static IdentificacionInvalidaException identificacionCorta() {
        return new IdentificacionInvalidaException("La identificación debe tener al menos 1 carácter");
    }

    /**
     * Factory method para crear excepción cuando identificación es muy larga
     */
    public static IdentificacionInvalidaException identificacionLarga() {
        return new IdentificacionInvalidaException("La identificación no puede exceder 20 caracteres");
    }

    /**
     * Factory method para crear excepción cuando identificación ya existe
     */
    public static IdentificacionInvalidaException identificacionDuplicada(String identificacion) {
        return new IdentificacionInvalidaException(
            "Ya existe un cliente con la identificación: " + identificacion
        );
    }
}
