package com.devsu.clientes.infrastructure.persistence.repository;

import com.devsu.clientes.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByIdentificacion(String identificacion);
    Optional<ClienteEntity> findByClienteId(Long clienteId);
}
