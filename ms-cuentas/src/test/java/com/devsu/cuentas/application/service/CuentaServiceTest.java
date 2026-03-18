package com.devsu.cuentas.application.service;

import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.domain.port.CuentaRepositoryPort;
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
class CuentaServiceTest {

    @Mock
    private CuentaRepositoryPort cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    @Test
    @DisplayName("crearCuenta - Cuando Saldo Actual es Null - Usa Saldo Inicial")
    void crearCuenta_CuandoNoHaySaldoActual_UsaSaldoInicial() {
        // Arrange
        String numeroCuenta = "123456";
        BigDecimal saldoInicial = new BigDecimal("1000");
        Cuenta cuenta = new Cuenta(numeroCuenta, "Ahorros", saldoInicial, true, 1L);
        
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Cuenta resultado = cuentaService.crearCuenta(cuenta);

        // Assert
        assertEquals(saldoInicial, resultado.getSaldoActual());
        verify(cuentaRepository, times(1)).save(cuenta);
    }

    @Test
    @DisplayName("obtenerCuentaPorNumero - Cuando Existe - Retorna Cuenta")
    void obtenerCuentaPorNumero_CuandoExiste_RetornaCuenta() {
        // Arrange
        String numeroCuenta = "123456";
        Cuenta cuenta = new Cuenta(numeroCuenta, "Ahorros", new BigDecimal("1000"), true, 1L);
        when(cuentaRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.of(cuenta));

        // Act
        Optional<Cuenta> resultado = cuentaService.obtenerCuentaPorNumero(numeroCuenta);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(numeroCuenta, resultado.get().getNumeroCuenta());
    }

    @Test
    @DisplayName("obtenerCuentaPorNumero - Cuando no Existe - Retorna Vacio")
    void obtenerCuentaPorNumero_CuandoNoExiste_RetornaVacio() {
        // Arrange
        String numero = "999";
        when(cuentaRepository.findByNumeroCuenta(numero)).thenReturn(Optional.empty());

        // Act
        Optional<Cuenta> resultado = cuentaService.obtenerCuentaPorNumero(numero);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("actualizarCuenta - Guarda Correctamente")
    void actualizarCuenta_GuardaCorrectamente() {
        // Arrange
        Cuenta cuenta = new Cuenta("123", "Corriente", new BigDecimal("500"), true, 1L);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        // Act
        Cuenta resultado = cuentaService.actualizarCuenta(cuenta);

        // Assert
        assertNotNull(resultado);
        verify(cuentaRepository, times(1)).save(cuenta);
    }

    @Test
    @DisplayName("eliminarCuenta - Llama al Repositorio")
    void eliminarCuenta_LlamaAlRepositorio() {
        // Arrange
        Long id = 1L;

        // Act
        cuentaService.eliminarCuenta(id);

        // Assert
        verify(cuentaRepository, times(1)).deleteById(id);
    }
}
