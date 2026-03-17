package com.devsu.cuentas.application.service;

import com.devsu.cuentas.application.dto.ReporteDTO;
import com.devsu.cuentas.application.port.in.ReporteUseCase;
import com.devsu.cuentas.domain.entity.Movimiento;
import com.devsu.cuentas.domain.port.MovimientoRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para Reportes (Requerimiento F4).
 */
public class ReporteService implements ReporteUseCase {

    private final MovimientoRepositoryPort movimientoRepository;

    public ReporteService(MovimientoRepositoryPort movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public List<ReporteDTO> generarReporte(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Movimiento> movimientos = movimientoRepository.findByClienteIdAndFechaBetween(clienteId, fechaInicio, fechaFin);
        
        return movimientos.stream()
                .map(mov -> {
                    ReporteDTO dto = new ReporteDTO();
                    dto.setFecha(mov.getFecha());
                    // Nota: El nombre del cliente debería obtenerse de ms-clientes. 
                    // Por ahora se deja el ID o se esperaría que el adapter de entrada lo complete.
                    dto.setCliente(mov.getCuenta().getClienteId().toString()); 
                    dto.setNumeroCuenta(mov.getCuenta().getNumeroCuenta());
                    dto.setTipo(mov.getCuenta().getTipoCuenta());
                    
                    // Saldo inicial para este movimiento = saldo actual - valor del movimiento
                    dto.setSaldoInicial(mov.getSaldo().subtract(mov.getValor()));
                    
                    dto.setEstado(mov.getCuenta().getEstado());
                    dto.setMovimiento(mov.getValor());
                    dto.setSaldoDisponible(mov.getSaldo());
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
