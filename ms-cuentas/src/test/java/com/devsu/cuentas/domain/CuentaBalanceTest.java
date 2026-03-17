package com.devsu.cuentas.domain;

import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.domain.exception.SaldoNoDisponibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba Unitaria (F5): Cobertura para la lógica de dominio de Cuenta.
 * Se enfoca en la validación de saldos y el manejo de excepciones de negocio.
 */
class CuentaBalanceTest {

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123456");
        cuenta.setSaldoActual(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Debe permitir un retiro cuando hay saldo suficiente")
    void testRetiroExitoso() {
        BigDecimal valorRetiro = new BigDecimal("400.00");
        
        cuenta.retirar(valorRetiro);
        
        assertEquals(new BigDecimal("600.00"), cuenta.getSaldoActual(), 
            "El saldo actual debería ser 600.00 después de retirar 400.00");
    }

    @Test
    @DisplayName("Debe lanzar SaldoNoDisponibleException cuando el retiro supera el saldo")
    void testRetiroInsuficiente() {
        BigDecimal valorRetiro = new BigDecimal("1000.01");
        
        assertThrows(SaldoNoDisponibleException.class, () -> cuenta.retirar(valorRetiro),
            "Debe lanzar SaldoNoDisponibleException por fondos insuficientes");
    }

    @Test
    @DisplayName("Debe permitir un depósito y actualizar el saldo")
    void testDepositoExitoso() {
        BigDecimal valorDeposito = new BigDecimal("500.00");
        
        cuenta.depositar(valorDeposito);
        
        assertEquals(new BigDecimal("1500.00"), cuenta.getSaldoActual(),
            "El saldo debería incrementarse a 1500.00");
    }

    @Test
    @DisplayName("Debe manejar retiros de valor negativo como valor absoluto")
    void testRetiroValorNegativo() {
        // En el dominio implementamos abs() para mayor robustez
        BigDecimal valorRetiro = new BigDecimal("-100.00");
        
        cuenta.retirar(valorRetiro);
        
        assertEquals(new BigDecimal("900.00"), cuenta.getSaldoActual(),
            "Debe tratar -100 como un retiro de 100");
    }

    @Test
    @DisplayName("Debe lanzar excepción con el mensaje exacto sugerido")
    void testMensajeExcepcionExacto() {
        BigDecimal valorRetiro = new BigDecimal("2000.00");
        
        SaldoNoDisponibleException exception = assertThrows(SaldoNoDisponibleException.class, 
            () -> cuenta.retirar(valorRetiro));
            
        assertEquals("Saldo no disponible", exception.getMessage(),
            "El mensaje de la excepción debe ser 'Saldo no disponible'");
    }
}
