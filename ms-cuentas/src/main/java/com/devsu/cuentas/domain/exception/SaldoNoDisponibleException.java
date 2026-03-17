package com.devsu.cuentas.domain.exception;

/**
 * Excepción de Negocio: Saldo No Disponible
 * 
 * Se lanza cuando se intenta realizar un retiro que supera el saldo actual
 * de la cuenta, cumpliendo el requerimiento F3.
 */
public class SaldoNoDisponibleException extends RuntimeException {
    
    public SaldoNoDisponibleException() {
        super("Saldo no disponible");
    }
    
    public SaldoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
