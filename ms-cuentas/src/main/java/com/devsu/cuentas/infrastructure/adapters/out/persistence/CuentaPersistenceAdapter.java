package com.devsu.cuentas.infrastructure.adapters.out.persistence;

import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.domain.port.CuentaRepositoryPort;
import com.devsu.cuentas.infrastructure.adapters.out.persistence.mapper.CuentaPersistenceMapper;
import com.devsu.cuentas.infrastructure.adapters.out.persistence.repository.JpaCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CuentaPersistenceAdapter implements CuentaRepositoryPort {

    private final JpaCuentaRepository jpaCuentaRepository;
    private final CuentaPersistenceMapper cuentaMapper;

    @Override
    public Cuenta save(Cuenta cuenta) {
        return cuentaMapper.toDomain(jpaCuentaRepository.save(cuentaMapper.toEntity(cuenta)));
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return jpaCuentaRepository.findById(id).map(cuentaMapper::toDomain);
    }

    @Override
    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        return jpaCuentaRepository.findByNumeroCuenta(numeroCuenta).map(cuentaMapper::toDomain);
    }

    @Override
    public List<Cuenta> findByClienteId(Long clienteId) {
        return jpaCuentaRepository.findByClienteId(clienteId).stream()
                .map(cuentaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cuenta> findAll() {
        return jpaCuentaRepository.findAll().stream()
                .map(cuentaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaCuentaRepository.deleteById(id);
    }
}
