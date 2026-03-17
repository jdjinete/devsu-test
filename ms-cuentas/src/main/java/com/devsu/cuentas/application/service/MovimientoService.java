package com.devsu.cuentas.application.service;

import com.devsu.cuentas.application.port.in.MovimientoUseCase;
import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.domain.entity.Movimiento;
import com.devsu.cuentas.domain.port.CuentaRepositoryPort;
import com.devsu.cuentas.domain.port.MovimientoRepositoryPort;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del caso de uso para Movimientos.
 * Orquesta la lógica de negocio requerida en F2 y F3.
 */
public class MovimientoService implements MovimientoUseCase {

    private final MovimientoRepositoryPort movimientoRepository;
    private final CuentaRepositoryPort cuentaRepository;

    public MovimientoService(MovimientoRepositoryPort movimientoRepository, CuentaRepositoryPort cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Movimiento registrarMovimiento(Movimiento movimiento) {
        // 1. Obtener la cuenta asociada
        String numeroCuenta = movimiento.getCuenta().getNumeroCuenta();
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));

        BigDecimal valor = movimiento.getValor();
        
        // 2. Aplicar el movimiento según el signo (F2)
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            // Retiro: La lógica de validación de saldo (F3) está dentro de cuenta.retirar()
            cuenta.retirar(valor);
            movimiento.setTipoMovimiento("Retiro");
        } else {
            // Depósito
            cuenta.depositar(valor);
            movimiento.setTipoMovimiento("Deposito");
        }

        // 3. Actualizar el saldo resultante en el movimiento
        movimiento.setSaldo(cuenta.getSaldoActual());
        movimiento.setCuenta(cuenta);

        // 4. Guardar la cuenta actualizada y el movimiento (Orquestación F2)
        // En una implementación real, esto debería estar bajo una transacción @Transactional
        // pero aquí mantenemos la lógica pura de aplicación.
        cuentaRepository.save(cuenta);
        return movimientoRepository.save(movimiento);
    }

    @Override
    public Optional<Movimiento> obtenerMovimientoPorId(Long id) {
        return movimientoRepository.findById(id);
    }

    @Override
    public List<Movimiento> obtenerTodos() {
        return movimientoRepository.findAll();
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorCuenta(String numeroCuenta) {
        return movimientoRepository.findByCuentaNumeroCuenta(numeroCuenta);
    }
}
