package com.devsu.cuentas.application.service;

import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.domain.entity.Movimiento;
import com.devsu.cuentas.domain.exception.SaldoNoDisponibleException;
import com.devsu.cuentas.domain.port.CuentaRepositoryPort;
import com.devsu.cuentas.domain.port.MovimientoRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepositoryPort movimientoRepository;

    @Mock
    private CuentaRepositoryPort cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    @Test
    @DisplayName("registrarMovimiento - Cuando es Deposito - Actualiza Saldo Correctamente")
    void registrarMovimiento_CuandoEsDeposito_ActualizaSaldoYGuarda() {
        // Arrange
        String numeroCuenta = "123456";
        BigDecimal saldoInicial = new BigDecimal("1000");
        BigDecimal montoDeposito = new BigDecimal("500");
        
        Cuenta cuenta = new Cuenta(numeroCuenta, "Ahorros", saldoInicial, true, 1L);
        cuenta.setSaldoActual(saldoInicial);

        Movimiento movimiento = new Movimiento();
        movimiento.setValor(montoDeposito);
        Cuenta cuentaRef = new Cuenta();
        cuentaRef.setNumeroCuenta(numeroCuenta);
        movimiento.setCuenta(cuentaRef);

        when(cuentaRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Movimiento resultado = movimientoService.registrarMovimiento(movimiento);

        // Assert
        assertEquals(new BigDecimal("1500"), resultado.getSaldo());
        assertEquals("Deposito", resultado.getTipoMovimiento());
        verify(cuentaRepository, times(1)).save(cuenta);
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("registrarMovimiento - Cuando es Retiro con Saldo Suficiente - Actualiza Saldo")
    void registrarMovimiento_CuandoEsRetiroSuficiente_ActualizaSaldoYGuarda() {
        // Arrange
        String numeroCuenta = "123456";
        BigDecimal saldoInicial = new BigDecimal("1000");
        BigDecimal montoRetiro = new BigDecimal("-300");
        
        Cuenta cuenta = new Cuenta(numeroCuenta, "Ahorros", saldoInicial, true, 1L);
        cuenta.setSaldoActual(saldoInicial);

        Movimiento movimiento = new Movimiento();
        movimiento.setValor(montoRetiro);
        Cuenta cuentaRef = new Cuenta();
        cuentaRef.setNumeroCuenta(numeroCuenta);
        movimiento.setCuenta(cuentaRef);

        when(cuentaRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Movimiento resultado = movimientoService.registrarMovimiento(movimiento);

        // Assert
        assertEquals(new BigDecimal("700"), resultado.getSaldo());
        assertEquals("Retiro", resultado.getTipoMovimiento());
        verify(cuentaRepository, times(1)).save(cuenta);
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("registrarMovimiento - Cuando es Retiro con Saldo Insuficiente - Lanza Excepcion")
    void registrarMovimiento_CuandoRetiroExcedeSaldo_LanzaSaldoNoDisponibleException() {
        // Arrange
        String numeroCuenta = "123456";
        BigDecimal saldoInicial = new BigDecimal("100");
        BigDecimal montoRetiro = new BigDecimal("-500");
        
        Cuenta cuenta = new Cuenta(numeroCuenta, "Ahorros", saldoInicial, true, 1L);
        cuenta.setSaldoActual(saldoInicial);

        Movimiento movimiento = new Movimiento();
        movimiento.setValor(montoRetiro);
        Cuenta cuentaRef = new Cuenta();
        cuentaRef.setNumeroCuenta(numeroCuenta);
        movimiento.setCuenta(cuentaRef);

        when(cuentaRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.of(cuenta));

        // Act & Assert
        assertThrows(SaldoNoDisponibleException.class, () -> {
            movimientoService.registrarMovimiento(movimiento);
        });
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarMovimiento - Cuando Aumenta el Valor del Movimiento - Corrige Saldo Cuenta")
    void actualizarMovimiento_CuandoSeEditaValor_RecalculaSaldoCorrectamente() {
        // Arrange
        Long movimientoId = 1L;
        String numeroCuenta = "123456";
        BigDecimal saldoCuentaActual = new BigDecimal("450"); // Saldo despues de un retiro de -50
        BigDecimal oldValor = new BigDecimal("-50");
        BigDecimal newValor = new BigDecimal("-100");

        Cuenta cuenta = new Cuenta(numeroCuenta, "Ahorros", new BigDecimal("500"), true, 1L);
        cuenta.setSaldoActual(saldoCuentaActual);

        Movimiento existingMov = new Movimiento();
        existingMov.setId(movimientoId);
        existingMov.setValor(oldValor);
        existingMov.setSaldo(saldoCuentaActual);
        existingMov.setCuenta(cuenta);

        Movimiento requestMov = new Movimiento();
        requestMov.setId(movimientoId);
        requestMov.setValor(newValor);
        requestMov.setCuenta(cuenta);

        when(movimientoRepository.findById(movimientoId)).thenReturn(Optional.of(existingMov));
        when(cuentaRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Movimiento resultado = movimientoService.actualizarMovimiento(requestMov);

        // Assert
        // Reversion: 450 - (-50) = 500. Aplicacion nuevo: 500 + (-100) = 400.
        assertEquals(new BigDecimal("400"), resultado.getSaldo());
        assertEquals(new BigDecimal("400"), cuenta.getSaldoActual());
        verify(cuentaRepository, times(1)).save(cuenta);
        verify(movimientoRepository, times(1)).save(existingMov);
    }

    @Test
    @DisplayName("actualizarMovimiento - Cuando Movimiento no Existe - Lanza Excepcion")
    void actualizarMovimiento_CuandoIdNoExiste_LanzaIllegalArgumentException() {
        // Arrange
        Long invalidId = 99L;
        Movimiento request = new Movimiento();
        request.setId(invalidId);

        when(movimientoRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.actualizarMovimiento(request);
        });
        assertTrue(ex.getMessage().contains("Movimiento no encontrado"));
        verify(movimientoRepository, never()).save(any());
    }
}
