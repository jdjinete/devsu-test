package com.devsu.cuentas.application.port.in;

import com.devsu.cuentas.domain.entity.Movimiento;
import java.util.List;
import java.util.Optional;

public interface MovimientoUseCase {
    Movimiento registrarMovimiento(Movimiento movimiento);
    Optional<Movimiento> obtenerMovimientoPorId(Long id);
    List<Movimiento> obtenerTodos();
    List<Movimiento> obtenerMovimientosPorCuenta(String numeroCuenta);
}
