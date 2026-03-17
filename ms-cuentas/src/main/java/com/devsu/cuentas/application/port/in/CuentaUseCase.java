package com.devsu.cuentas.application.port.in;

import com.devsu.cuentas.domain.entity.Cuenta;
import java.util.List;
import java.util.Optional;

public interface CuentaUseCase {
    Cuenta crearCuenta(Cuenta cuenta);
    Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta);
    Optional<Cuenta> obtenerCuentaPorId(Long id);
    List<Cuenta> obtenerTodas();
    Cuenta actualizarCuenta(Cuenta cuenta);
    void eliminarCuenta(Long id);
    List<Cuenta> obtenerCuentasPorCliente(Long clienteId);
}
