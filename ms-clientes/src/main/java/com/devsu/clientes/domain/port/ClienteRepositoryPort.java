package com.devsu.clientes.domain.port;

import com.devsu.clientes.domain.entity.Cliente;
import java.util.Optional;
import java.util.List;

/**
 * Puerto de Salida: ClienteRepositoryPort
 *
 * Define el contrato para las operaciones de persistencia de clientes.
 * Esta interfaz separa la lógica de dominio de los detalles técnicos
 * de la persistencia (JPA, SQL, etc.).
 *
 * Implementación: Se proporcionará en la capa de infraestructura.
 *
 * Patrones:
 * - Retorna Optional<T> para consultas que podrían no encontrar registros
 * - Usa excepciones de negocio para violaciones de reglas
 * - Define contrato, no implementación
 *
 * @author Devsu
 * @version 1.0
 */
public interface ClienteRepositoryPort {

    /**
     * Guarda un nuevo cliente en la persistencia.
     * Si el cliente ya existe (por identificación), lanza excepción.
     *
     * @param cliente Cliente a guardar (sin clienteId generado aún)
     * @return ID asignado al cliente por la persistencia
     * @throws IllegalArgumentException si el cliente es nulo
     */
    Long guardar(Cliente cliente);

    /**
     * Actualiza un cliente existente en la persistencia.
     *
     * @param cliente Cliente con datos actualizados (debe tener clienteId)
     * @throws IllegalArgumentException si el cliente es nulo o no tiene clienteId
     */
    void actualizar(Cliente cliente);

    /**
     * Obtiene un cliente por su ID único.
     *
     * @param clienteId ID del cliente a buscar
     * @return Optional con el cliente si existe, vacío si no
     * @throws IllegalArgumentException si clienteId es nulo o negativo
     */
    Optional<Cliente> obtenerPorId(Long clienteId);

    /**
     * Obtiene un cliente por su identificación (documento de identidad).
     * La identificación es única en el sistema.
     *
     * @param identificacion Identificación del cliente (cédula, RUC, etc.)
     * @return Optional con el cliente si existe, vacío si no
     * @throws IllegalArgumentException si identificacion es nula o vacía
     */
    Optional<Cliente> obtenerPorIdentificacion(String identificacion);

    /**
     * Verifica si existe un cliente con la identificación dada.
     *
     * @param identificacion Identificación a verificar
     * @return true si existe un cliente con esa identificación
     * @throws IllegalArgumentException si identificacion es nula o vacía
     */
    boolean existePorIdentificacion(String identificacion);

    /**
     * Obtiene todos los clientes del sistema.
     * En aplicaciones con muchos registros, considerar paginación en futuras versiones.
     *
     * @return Lista de todos los clientes
     */
    List<Cliente> obtenerTodos();

    /**
     * Obtiene todos los clientes activos.
     *
     * @return Lista de clientes con estado ACTIVO
     */
    List<Cliente> obtenerActivos();

    /**
     * Obtiene todos los clientes inactivos.
     *
     * @return Lista de clientes con estado INACTIVO
     */
    List<Cliente> obtenerInactivos();

    /**
     * Realiza un "soft delete": marca el cliente como inactivo.
     * No elimina físicamente el registro de la base de datos.
     * Se usa para mantener integridad con historial.
     *
     * @param clienteId ID del cliente a "eliminar"
     * @throws IllegalArgumentException si clienteId es nulo
     */
    void eliminar(Long clienteId);

    /**
     * Obtiene la cantidad total de clientes.
     *
     * @return Número de clientes registrados
     */
    long contar();

    /**
     * Obtiene la cantidad de clientes activos.
     *
     * @return Número de clientes con estado ACTIVO
     */
    long contarActivos();
}
