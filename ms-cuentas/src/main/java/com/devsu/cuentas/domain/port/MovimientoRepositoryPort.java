package com.devsu.cuentas.domain.port;

import com.devsu.cuentas.domain.entity.Movimiento;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de Salida: MovimientoRepositoryPort
 * 
 * Interfaz para la persistencia de movimientos bancarios.
 */
public interface MovimientoRepositoryPort {
    
    Movimiento save(Movimiento movimiento);
    
    Optional<Movimiento> findById(Long id);
    
    List<Movimiento> findAll();
    
    List<Movimiento> findByCuentaNumeroCuenta(String numeroCuenta);
    
    /**
     * Busca movimientos por cliente y rango de fechas para reportes.
     */
    List<Movimiento> findByClienteIdAndFechaBetween(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
