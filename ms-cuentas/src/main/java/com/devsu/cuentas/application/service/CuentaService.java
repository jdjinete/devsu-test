package com.devsu.cuentas.application.service;

import com.devsu.cuentas.application.port.in.CuentaUseCase;
import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.domain.port.CuentaRepositoryPort;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del caso de uso para Cuentas.
 * Sin anotaciones de framework para mantener la pureza de la capa de aplicación.
 */
public class CuentaService implements CuentaUseCase {

    private final CuentaRepositoryPort cuentaRepository;

    public CuentaService(CuentaRepositoryPort cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Cuenta crearCuenta(Cuenta cuenta) {
        // Por defecto el saldo actual es el saldo inicial al crear
        if (cuenta.getSaldoActual() == null) {
            cuenta.setSaldoActual(cuenta.getSaldoInicial());
        }
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public Optional<Cuenta> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    public List<Cuenta> obtenerTodas() {
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta actualizarCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    public void eliminarCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }

    @Override
    public List<Cuenta> obtenerCuentasPorCliente(Long clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }
}
