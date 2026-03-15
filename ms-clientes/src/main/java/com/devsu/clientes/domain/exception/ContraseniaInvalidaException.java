package com.devsu.clientes.domain.exception;

/**
 * Excepción: ContraseniaInvalidaException
 *
 * Se lanza cuando la contraseña no cumple con los requisitos de seguridad
 * del dominio.
 *
 * Requisitos de contraseña:
 * - Mínimo 8 caracteres
 * - Máximo 50 caracteres
 * - No puede estar vacía
 *
 * Casos de uso:
 * - Crear cliente con contraseña demasiado corta
 * - Cambiar contraseña a una inválida
 * - Intentar login con valores vacíos
 *
 * @author Devsu
 * @version 1.0
 */
public class ContraseniaInvalidaException extends ClienteDomainException {

    public ContraseniaInvalidaException(String mensaje) {
        super(mensaje);
    }

    public ContraseniaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Factory method para crear excepción cuando contraseña es vacía
     */
    public static ContraseniaInvalidaException contraseniaVacia() {
        return new ContraseniaInvalidaException("La contraseña no puede estar vacía");
    }

    /**
     * Factory method para crear excepción cuando contraseña es muy corta
     */
    public static ContraseniaInvalidaException contraseniaCorta() {
        return new ContraseniaInvalidaException("La contraseña debe tener al menos 8 caracteres");
    }

    /**
     * Factory method para crear excepción cuando contraseña es muy larga
     */
    public static ContraseniaInvalidaException contraseniaLarga() {
        return new ContraseniaInvalidaException("La contraseña no puede exceder 50 caracteres");
    }
}
