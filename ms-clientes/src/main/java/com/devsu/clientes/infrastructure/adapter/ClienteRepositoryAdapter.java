package com.devsu.clientes.infrastructure.adapter;

import com.devsu.clientes.domain.entity.Cliente;
import com.devsu.clientes.domain.port.ClienteRepositoryPort;
import com.devsu.clientes.infrastructure.persistence.entity.ClienteEntity;
import com.devsu.clientes.infrastructure.persistence.repository.JpaClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final JpaClienteRepository jpaClienteRepository;
    // MapStruct mapper could be injected here
    // private final ClientePersistenceMapper mapper;

    @Override
    public Long guardar(Cliente cliente) {
        ClienteEntity entity = toEntity(cliente);
        ClienteEntity savedEntity = jpaClienteRepository.save(entity);
        return savedEntity.getClienteId();
    }

    @Override
    public void actualizar(Cliente cliente) {
        if (cliente.getClienteId() == null) {
            throw new IllegalArgumentException("El cliente debe tener un ID para ser actualizado");
        }
        ClienteEntity entity = toEntity(cliente);
        jpaClienteRepository.save(entity);
    }

    @Override
    public Optional<Cliente> obtenerPorId(Long clienteId) {
        return jpaClienteRepository.findByClienteId(clienteId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Cliente> obtenerPorIdentificacion(String identificacion) {
        return jpaClienteRepository.findByIdentificacion(identificacion)
                .map(this::toDomain);
    }

    @Override
    public boolean existePorIdentificacion(String identificacion) {
        return jpaClienteRepository.findByIdentificacion(identificacion).isPresent();
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return jpaClienteRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> obtenerActivos() {
        return jpaClienteRepository.findAll().stream()
                .filter(c -> "ACTIVO".equals(c.getEstadoCliente()))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> obtenerInactivos() {
        return jpaClienteRepository.findAll().stream()
                .filter(c -> "INACTIVO".equals(c.getEstadoCliente()))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long clienteId) {
        jpaClienteRepository.findByClienteId(clienteId).ifPresent(entity -> {
            entity.setEstadoCliente("INACTIVO");
            jpaClienteRepository.save(entity);
        });
    }

    @Override
    public long contar() {
        return jpaClienteRepository.count();
    }

    @Override
    public long contarActivos() {
        return jpaClienteRepository.findAll().stream()
                .filter(c -> "ACTIVO".equals(c.getEstadoCliente()))
                .count();
    }

    // Manual mapping for now, but ready to be replaced by MapStruct
    private ClienteEntity toEntity(Cliente domain) {
        if (domain == null) return null;
        ClienteEntity entity = new ClienteEntity();
        // Since PersonaEntity fields are protected in domain, we use getters
        entity.setPersonaId(domain.getPersonaId());
        entity.setNombre(domain.getNombre());
        entity.setGenero(domain.getGenero());
        entity.setEdad(domain.getEdad());
        entity.setIdentificacion(domain.getIdentificacion());
        entity.setDireccion(domain.getDireccion());
        entity.setTelefono(domain.getTelefono());
        entity.setEstado(domain.getEstado());
        entity.setFechaRegistro(domain.getFechaRegistro());
        if (domain.getClienteId() == null) {
            entity.setClienteId(System.currentTimeMillis()); // Simulación de generación
        } else {
            entity.setClienteId(domain.getClienteId());
        }
        entity.setContrasena(domain.getContrasena());
        entity.setEstadoCliente(domain.getEstadoCliente());
        entity.setUltimoAcceso(domain.getUltimoAcceso());
        return entity;
    }

    private Cliente toDomain(ClienteEntity entity) {
        if (entity == null) return null;
        return Cliente.crear(
                entity.getNombre(),
                entity.getGenero(),
                entity.getEdad(),
                entity.getIdentificacion(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getContrasena()
        );
    }
}
