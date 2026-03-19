package com.devsu.cuentas.infrastructure.adapters.out.persistence.repository;

import com.devsu.cuentas.infrastructure.adapters.out.persistence.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JpaCuentaRepository extends JpaRepository<CuentaEntity, Long> {
    Optional<CuentaEntity> findByNumeroCuenta(String numeroCuenta);
    List<CuentaEntity> findByClienteId(Long clienteId);
}
