package com.devsu.clientes.infrastructure.adapters.out.persistence.repository;

import com.devsu.clientes.infrastructure.adapters.out.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByIdentificacion(String identificacion);
    Optional<ClienteEntity> findByClienteId(Long clienteId);

    @Query("SELECT MAX(c.clienteId) FROM ClienteEntity c")
    Optional<Long> findMaxClienteId();
}
