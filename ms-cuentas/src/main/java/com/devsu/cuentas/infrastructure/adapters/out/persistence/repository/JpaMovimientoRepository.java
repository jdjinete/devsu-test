package com.devsu.cuentas.infrastructure.adapters.out.persistence.repository;

import com.devsu.cuentas.infrastructure.adapters.out.persistence.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface JpaMovimientoRepository extends JpaRepository<MovimientoEntity, Long> {
    List<MovimientoEntity> findByCuentaNumeroCuenta(String numeroCuenta);

    @Query("SELECT m FROM MovimientoEntity m JOIN m.cuenta c WHERE c.clienteId = :clienteId AND m.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<MovimientoEntity> findByClienteIdAndFechaBetween(
            @Param("clienteId") Long clienteId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}
