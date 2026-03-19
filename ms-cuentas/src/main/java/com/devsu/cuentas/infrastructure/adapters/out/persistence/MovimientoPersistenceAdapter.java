package com.devsu.cuentas.infrastructure.adapters.out.persistence;

import com.devsu.cuentas.domain.entity.Movimiento;
import com.devsu.cuentas.domain.port.MovimientoRepositoryPort;
import com.devsu.cuentas.infrastructure.adapters.out.persistence.mapper.CuentaPersistenceMapper;
import com.devsu.cuentas.infrastructure.adapters.out.persistence.mapper.MovimientoPersistenceMapper;
import com.devsu.cuentas.infrastructure.adapters.out.persistence.repository.JpaMovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovimientoPersistenceAdapter implements MovimientoRepositoryPort {

    private final JpaMovimientoRepository jpaMovimientoRepository;
    private final MovimientoPersistenceMapper movimientoMapper;
    private final CuentaPersistenceMapper cuentaMapper;

    @Override
    public Movimiento save(Movimiento movimiento) {
        return movimientoMapper.toDomainWithCuenta(
                jpaMovimientoRepository.save(movimientoMapper.toEntity(movimiento)),
                cuentaMapper
        );
    }

    @Override
    public Optional<Movimiento> findById(Long id) {
        return jpaMovimientoRepository.findById(id)
                .map(entity -> movimientoMapper.toDomainWithCuenta(entity, cuentaMapper));
    }

    @Override
    public List<Movimiento> findAll() {
        return jpaMovimientoRepository.findAll().stream()
                .map(entity -> movimientoMapper.toDomainWithCuenta(entity, cuentaMapper))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movimiento> findByCuentaNumeroCuenta(String numeroCuenta) {
        return jpaMovimientoRepository.findByCuentaNumeroCuenta(numeroCuenta).stream()
                .map(entity -> movimientoMapper.toDomainWithCuenta(entity, cuentaMapper))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movimiento> findByClienteIdAndFechaBetween(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return jpaMovimientoRepository.findByClienteIdAndFechaBetween(clienteId, fechaInicio, fechaFin).stream()
                .map(entity -> movimientoMapper.toDomainWithCuenta(entity, cuentaMapper))
                .collect(Collectors.toList());
    }
}
