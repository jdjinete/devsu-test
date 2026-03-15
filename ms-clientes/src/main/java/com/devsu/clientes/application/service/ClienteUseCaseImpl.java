package com.devsu.clientes.application.service;

import com.devsu.clientes.domain.entity.Cliente;
import com.devsu.clientes.domain.exception.ClienteNoEncontradoException;
import com.devsu.clientes.domain.exception.ClienteYaExisteException;
import com.devsu.clientes.domain.port.ClienteEventPublisherPort;
import com.devsu.clientes.domain.port.ClienteRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: ClienteUseCaseImpl (Implementación)
 *
 * Orquesta la lógica de negocio para gestión completa de clientes.
 * Coordina:
 * - Validaciones de reglas de negocio
 * - Operaciones de persistencia (a través de ClienteRepositoryPort)
 * - Publicación de eventos de dominio (a través de ClienteEventPublisherPort)
 * - Transaccionalidad (garantiza atomicidad de operaciones)
 *
 * Dependencias inyectadas:
 * - ClienteRepositoryPort: para acceso a datos
 * - ClienteEventPublisherPort: para publicación asincrónica de eventos
 *
 * @author Devsu
 * @version 1.0
 */
@Service
@Transactional
public class ClienteUseCaseImpl implements ClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ClienteEventPublisherPort clienteEventPublisherPort;

    /**
     * Constructor con inyección de dependencias por constructor.
     * Spring inyecta automáticamente las implementaciones de los puertos.
     *
     * @param clienteRepositoryPort Puerto para operaciones de persistencia
     * @param clienteEventPublisherPort Puerto para publicación de eventos
     */
    public ClienteUseCaseImpl(ClienteRepositoryPort clienteRepositoryPort,
                            ClienteEventPublisherPort clienteEventPublisherPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.clienteEventPublisherPort = clienteEventPublisherPort;
    }

    /**
     * Crea un nuevo cliente en el sistema.
     *
     * Flujo de ejecución:
     * 1. Valida que identificación no sea duplicada
     * 2. Crea entidad Cliente en el dominio
     * 3. Persiste en la base de datos
     * 4. Publica evento ClienteCreado (async)
     *
     * @param nombre Nombre del cliente
     * @param genero Género (M/F/O)
     * @param edad Edad en años (0-150)
     * @param identificacion Identificación única
     * @param direccion Dirección de residencia (1-200 caracteres)
     * @param telefono Número de teléfono (1-20 caracteres)
     * @param contrasena Contraseña (8-50 caracteres)
     * @return Cliente creado con clienteId asignado
     * @throws ClienteYaExisteException si identificación ya existe
     * @throws IllegalArgumentException si algún campo es inválido
     */
    @Override
    public Cliente crearCliente(String nombre, String genero, Integer edad,
                               String identificacion, String direccion,
                               String telefono, String contrasena) {

        // Validar que identificación no sea duplicada
        if (clienteRepositoryPort.existePorIdentificacion(identificacion)) {
            throw new ClienteYaExisteException(identificacion);
        }

        // Crear entidad en el dominio (usa factory method)
        Cliente cliente = Cliente.crear(nombre, genero, edad, identificacion,
                                       direccion, telefono, contrasena);

        // Persistir en base de datos (obtiene clienteId asignado)
        Long clienteId = clienteRepositoryPort.guardar(cliente);
        cliente = clienteRepositoryPort.obtenerPorId(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));

        // Publicar evento (async - notifica otros microservicios)
        clienteEventPublisherPort.publicarClienteCreado(cliente);

        return cliente;
    }

    /**
     * Obtiene un cliente por su ID único.
     *
     * @param clienteId ID del cliente a recuperar
     * @return Optional con el cliente si existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerCliente(Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("clienteId no puede ser nulo ni negativo");
        }
        return clienteRepositoryPort.obtenerPorId(clienteId);
    }

    /**
     * Obtiene un cliente por su identificación.
     *
     * @param identificacion Identificación del cliente
     * @return Optional con el cliente si existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerClientePorIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificación no puede estar vacía");
        }
        return clienteRepositoryPort.obtenerPorIdentificacion(identificacion);
    }

    /**
     * Obtiene todos los clientes del sistema.
     *
     * @return Lista de todos los clientes
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepositoryPort.obtenerTodos();
    }

    /**
     * Obtiene todos los clientes activos.
     *
     * @return Lista de clientes con estado ACTIVO
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientesActivos() {
        return clienteRepositoryPort.obtenerActivos();
    }

    /**
     * Obtiene todos los clientes inactivos.
     *
     * @return Lista de clientes con estado INACTIVO
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientesInactivos() {
        return clienteRepositoryPort.obtenerInactivos();
    }

    /**
     * Actualiza información personal de un cliente existente.
     *
     * Flujo de ejecución:
     * 1. Obtiene cliente por ID (lanza excepción si no existe)
     * 2. Actualiza campos proporcionados
     * 3. Persiste cambios
     * 4. Publica evento ClienteActualizado (async)
     *
     * @param clienteId ID del cliente a actualizar
     * @param nombre Nuevo nombre (null para no cambiar)
     * @param edad Nueva edad (null para no cambiar)
     * @param direccion Nueva dirección (null para no cambiar)
     * @param telefono Nuevo teléfono (null para no cambiar)
     * @return Cliente actualizado
     * @throws ClienteNoEncontradoException si cliente no existe
     * @throws IllegalArgumentException si nuevos datos son inválidos
     */
    @Override
    public Cliente actualizarCliente(Long clienteId, String nombre, Integer edad,
                                    String direccion, String telefono) {

        // Obtener cliente existente
        Cliente cliente = clienteRepositoryPort.obtenerPorId(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));

        // Actualizar campos si se proporcionan
        cliente.actualizar(nombre, cliente.getGenero(), edad, direccion, telefono);

        // Persistir cambios
        clienteRepositoryPort.actualizar(cliente);

        // Publicar evento
        clienteEventPublisherPort.publicarClienteActualizado(cliente);

        return cliente;
    }

    /**
     * Cambia la contraseña de un cliente.
     *
     * Flujo de ejecución:
     * 1. Obtiene cliente por ID
     * 2. Valida nueva contraseña en el dominio
     * 3. Cambia contraseña
     * 4. Persiste cambios
     * 5. Publica evento ContraseniaActualizada (async)
     *
     * @param clienteId ID del cliente
     * @param nuevaContrasena Nueva contraseña (8-50 caracteres)
     * @throws ClienteNoEncontradoException si cliente no existe
     * @throws IllegalArgumentException si contraseña no es válida
     */
    @Override
    public void cambiarContrasena(Long clienteId, String nuevaContrasena) {

        // Obtener cliente existente
        Cliente cliente = clienteRepositoryPort.obtenerPorId(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));

        // Cambia contraseña (valida en el dominio)
        cliente.cambiarContrasena(nuevaContrasena);

        // Persistir cambios
        clienteRepositoryPort.actualizar(cliente);

        // Publicar evento
        clienteEventPublisherPort.publicarContraseniaActualizada(clienteId);
    }

    /**
     * Activa un cliente (cambia a estado ACTIVO).
     *
     * Flujo de ejecución:
     * 1. Obtiene cliente por ID
     * 2. Lo activa (valida estado en el dominio)
     * 3. Persiste cambios
     * 4. Publica evento ClienteHabilitado (async)
     * 5. ms-cuentas consume el evento y reactiva cuentas del cliente
     *
     * @param clienteId ID del cliente a activar
     * @throws ClienteNoEncontradoException si cliente no existe
     * @throws IllegalStateException si cliente ya está activo
     */
    @Override
    public void habilitarCliente(Long clienteId) {

        Cliente cliente = clienteRepositoryPort.obtenerPorId(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));

        // Activar (en dominio, valida que no esté ya activo)
        cliente.activar();

        // Persistir
        clienteRepositoryPort.actualizar(cliente);

        // Publicar evento - ms-cuentas lo consume para reactivar cuentas
        clienteEventPublisherPort.publicarClienteHabilitado(
                cliente.getClienteId(),
                cliente.getIdentificacion()
        );
    }

    /**
     * Inactiva un cliente (cambia a estado INACTIVO).
     *
     * Flujo de ejecución:
     * 1. Obtiene cliente por ID
     * 2. Lo inactiva (valida estado en el dominio)
     * 3. Persiste cambios
     * 4. Publica evento ClienteInhabilitado (async)
     * 5. ms-cuentas consume el evento y desactiva cuentas del cliente
     *
     * @param clienteId ID del cliente a inactivar
     * @throws ClienteNoEncontradoException si cliente no existe
     * @throws IllegalStateException si cliente ya está inactivo
     */
    @Override
    public void inhabilitarCliente(Long clienteId) {

        Cliente cliente = clienteRepositoryPort.obtenerPorId(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));

        // Inactivar (en dominio, valida que no esté ya inactivo)
        cliente.inactivar();

        // Persistir
        clienteRepositoryPort.actualizar(cliente);

        // Publicar evento - ms-cuentas lo consume para desactivar cuentas
        clienteEventPublisherPort.publicarClienteInhabilitado(
                cliente.getClienteId(),
                cliente.getIdentificacion()
        );
    }

    /**
     * Bloquea un cliente por razones de seguridad.
     *
     * Flujo de ejecución:
     * 1. Obtiene cliente por ID
     * 2. Lo bloquea (valida estado en el dominio)
     * 3. Persiste cambios
     * 4. Publica evento ClienteBloqueado (async)
     * 5. ms-cuentas consume el evento y bloquea cuentas del cliente
     *
     * @param clienteId ID del cliente a bloquear
     * @throws ClienteNoEncontradoException si cliente no existe
     * @throws IllegalStateException si cliente ya está bloqueado
     */
    @Override
    public void bloquearCliente(Long clienteId) {

        Cliente cliente = clienteRepositoryPort.obtenerPorId(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));

        // Bloquear (en dominio, valida que no esté ya bloqueado)
        cliente.bloquear();

        // Persistir
        clienteRepositoryPort.actualizar(cliente);

        // Publicar evento - ms-cuentas lo consume para bloquear cuentas
        clienteEventPublisherPort.publicarClienteBloqueado(
                cliente.getClienteId(),
                cliente.getIdentificacion()
        );
    }

    /**
     * Desbloquea un cliente después de resolver el incidente.
     *
     * Flujo de ejecución:
     * 1. Obtiene cliente por ID
     * 2. Lo desbloquea (valida estado en el dominio)
     * 3. Persiste cambios
     * 4. Publica evento ClienteDesbloqueado (async)
     * 5. ms-cuentas consume el evento y desbloquea cuentas del cliente
     *
     * @param clienteId ID del cliente a desbloquear
     * @throws ClienteNoEncontradoException si cliente no existe
     * @throws IllegalStateException si cliente no está bloqueado
     */
    @Override
    public void desbloquearCliente(Long clienteId) {

        Cliente cliente = clienteRepositoryPort.obtenerPorId(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));

        // Desbloquear (en dominio, valida que esté bloqueado)
        cliente.desbloquear();

        // Persistir
        clienteRepositoryPort.actualizar(cliente);

        // Publicar evento - ms-cuentas lo consume para desbloquear cuentas
        clienteEventPublisherPort.publicarClienteDesbloqueado(
                cliente.getClienteId(),
                cliente.getIdentificacion()
        );
    }

    /**
     * Elimina lógicamente un cliente (soft delete).
     * El registro permanece en BD para auditoría.
     *
     * Flujo de ejecución:
     * 1. Obtiene cliente por ID
     * 2. Lo marca como INACTIVO (es el soft delete)
     * 3. Persiste cambios
     * 4. Publica evento ClienteEliminado (async)
     * 5. ms-cuentas consume el evento y elimina lógicamente cuentas del cliente
     *
     * @param clienteId ID del cliente a eliminar
     * @throws ClienteNoEncontradoException si cliente no existe
     */
    @Override
    public void eliminarCliente(Long clienteId) {

        Cliente cliente = clienteRepositoryPort.obtenerPorId(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException(clienteId));

        // Soft delete usando el puerto de repositorio
        clienteRepositoryPort.eliminar(clienteId);

        // Publicar evento - ms-cuentas lo consume para eliminar cuentas lógicamente
        clienteEventPublisherPort.publicarClienteEliminado(
                cliente.getClienteId(),
                cliente.getIdentificacion()
        );
    }

    /**
     * Obtiene la cantidad total de clientes.
     *
     * @return Número de clientes registrados
     */
    @Override
    @Transactional(readOnly = true)
    public long contarClientesTotales() {
        return clienteRepositoryPort.contar();
    }

    /**
     * Obtiene la cantidad de clientes activos.
     *
     * @return Número de clientes con estado ACTIVO
     */
    @Override
    @Transactional(readOnly = true)
    public long contarClientesActivos() {
        return clienteRepositoryPort.contarActivos();
    }
}
