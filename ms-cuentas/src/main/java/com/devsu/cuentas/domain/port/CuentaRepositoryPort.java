package com.devsu.cuentas.domain.port;

import com.devsu.cuentas.domain.entity.Cuenta;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de Salida: CuentaRepositoryPort
 * 
 * Interfaz para la persistencia de cuentas bancarias.
 */
public interface CuentaRepositoryPort {
    
    Cuenta save(Cuenta cuenta);
    
    Optional<Cuenta> findById(Long id);
    
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    
    List<Cuenta> findByClienteId(Long clienteId);
    
    List<Cuenta> findAll();
    
    void deleteById(Long id);
}
