package com.devsu.clientes.application.service;

import com.devsu.clientes.domain.entity.Cliente;
import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: ClienteUseCase (Interfaz)
 *
 * Define el contrato de orquestación para la gestión completa de clientes.
 * Esta interfaz coordina la lógica transversal y las reglas de negocio
 * usando los puertos del dominio (repositorio y publicador de eventos).
 *
 * Responsabilidades:
 * - CRUD (Crear, Leer, Actualizar, Borrar/Inactivar) de clientes
 * - Validaciones de negocio (unicidad, campos requeridos)
 * - Publicación de eventos de dominio (creación, actualización, inactivación, etc.)
 * - Transaccionalidad coordinada
 *
 * Implementación: Se proporciona mediante ClienteUseCaseImpl.
 * Inyección: Se realiza en la capa de infraestructura (Controllers, Services).
 *
 * @author Devsu
 * @version 1.0
 */
public interface ClienteUseCase {

    /**
     * Crea un nuevo cliente en el sistema.
     *
     * Validaciones:
     * - Identificación no debe existir (uniqueness)
     * - Todos los campos requeridos deben ser válidos
     *
     * Acciones:
     * - Crea la entidad Cliente en el dominio
     * - Persiste en el repositorio
     * - Publica evento ClienteCreado
     *
     * @param nombre Nombre del cliente
     * @param genero Género (M/F/O)
     * @param edad Edad en años
     * @param identificacion Identificación única (cédula, RUC, etc.)
     * @param direccion Dirección de residencia
     * @param telefono Número de teléfono
     * @param contrasena Contraseña del cliente
     * @return Cliente creado con clienteId asignado
     * @throws IllegalArgumentException si algún campo es inválido
     * @throws com.devsu.clientes.domain.exception.ClienteYaExisteException si identificación ya existe
     */
    Cliente crearCliente(String nombre, String genero, Integer edad,
                        String identificacion, String direccion,
                        String telefono, String contrasena);

    /**
     * Obtiene un cliente por su ID único.
     *
     * @param clienteId ID del cliente a recuperar
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> obtenerCliente(Long clienteId);

    /**
     * Obtiene un cliente por su identificación (documento de identidad).
     *
     * @param identificacion Identificación del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> obtenerClientePorIdentificacion(String identificacion);

    /**
     * Obtiene todos los clientes registrados en el sistema.
     *
     * @return Lista de todos los clientes
     */
    List<Cliente> obtenerTodosLosClientes();

    /**
     * Obtiene todos los clientes con estado ACTIVO.
     *
     * @return Lista de clientes activos
     */
    List<Cliente> obtenerClientesActivos();

    /**
     * Obtiene todos los clientes con estado INACTIVO.
     *
     * @return Lista de clientes inactivos
     */
    List<Cliente> obtenerClientesInactivos();

    /**
     * Actualiza la información personal de un cliente existente.
     *
     * Validaciones:
     * - Cliente debe existir
     * - Cliente no debe estar completamente inactivo (estado general)
     * - Nuevos datos deben ser válidos
     *
     * Acciones:
     * - Actualiza la entidad en memoria
     * - Persiste cambios
     * - Publica evento ClienteActualizado
     *
     * @param clienteId ID del cliente a actualizar
     * @param nombre Nuevo nombre (o null para no cambiar)
     * @param edad Nueva edad (o null para no cambiar)
     * @param direccion Nueva dirección (o null para no cambiar)
     * @param telefono Nuevo teléfono (o null para no cambiar)
     * @return Cliente actualizado
     * @throws com.devsu.clientes.domain.exception.ClienteNoEncontradoException si cliente no existe
     * @throws IllegalArgumentException si nuevos datos son inválidos
     */
    Cliente actualizarCliente(Long clienteId, String nombre, String genero, Integer edad,
                             String direccion, String telefono);

    /**
     * Cambia la contraseña de un cliente.
     *
     * Validaciones:
     * - Cliente debe existir
     * - Nueva contraseña debe cumplir requisitos (8-50 caracteres)
     * - Nueva contraseña no puede ser igual a la actual
     *
     * Acciones:
     * - Actualiza contraseña en la entidad
     * - Persiste cambios
     * - Publica evento ContraseniaActualizada
     *
     * @param clienteId ID del cliente
     * @param nuevaContrasena Nueva contraseña
     * @throws com.devsu.clientes.domain.exception.ClienteNoEncontradoException si cliente no existe
     * @throws com.devsu.clientes.domain.exception.ContraseniaInvalidaException si contraseña no es válida
     */
    void cambiarContrasena(Long clienteId, String nuevaContrasena);

    /**
     * Activa un cliente (lo cambia a estado ACTIVO).
     * Un cliente activo puede acceder al sistema y realizar operaciones.
     *
     * Acciones:
     * - Cambia estado a ACTIVO
     * - Persiste cambios
     * - Publica evento ClienteHabilitado
     * - Notifica a ms-cuentas para reactivar cuentas asociadas
     *
     * @param clienteId ID del cliente a activar
     * @throws com.devsu.clientes.domain.exception.ClienteNoEncontradoException si cliente no existe
     * @throws IllegalStateException si cliente ya está activo
     */
    void habilitarCliente(Long clienteId);

    /**
     * Inactiva un cliente (lo cambia a estado INACTIVO).
     * Un cliente inactivo no puede acceder al sistema ni realizar operaciones.
     *
     * Acciones:
     * - Cambia estado a INACTIVO
     * - Persiste cambios
     * - Publica evento ClienteInhabilitado
     * - Notifica a ms-cuentas para desactivar cuentas asociadas
     *
     * @param clienteId ID del cliente a inactivar
     * @throws com.devsu.clientes.domain.exception.ClienteNoEncontradoException si cliente no existe
     * @throws IllegalStateException si cliente ya está inactivo
     */
    void inhabilitarCliente(Long clienteId);

    /**
     * Bloquea un cliente (por razones de seguridad o fraude).
     * Un cliente bloqueado no puede acceder al sistema.
     *
     * Acciones:
     * - Cambia estado a BLOQUEADO
     * - Persiste cambios
     * - Publica evento ClienteBloqueado
     * - Notifica a ms-cuentas para bloquear cuentas asociadas
     *
     * @param clienteId ID del cliente a bloquear
     * @throws com.devsu.clientes.domain.exception.ClienteNoEncontradoException si cliente no existe
     * @throws IllegalStateException si cliente ya está bloqueado
     */
    void bloquearCliente(Long clienteId);

    /**
     * Desbloquea un cliente (después de resolver incidente de seguridad).
     * Un cliente desbloqueado vuelve al estado ACTIVO y puede operar.
     *
     * Acciones:
     * - Cambia estado a ACTIVO
     * - Persiste cambios
     * - Publica evento ClienteDesbloqueado
     * - Notifica a ms-cuentas para desbloquear cuentas asociadas
     *
     * @param clienteId ID del cliente a desbloquear
     * @throws com.devsu.clientes.domain.exception.ClienteNoEncontradoException si cliente no existe
     * @throws IllegalStateException si cliente no está bloqueado
     */
    void desbloquearCliente(Long clienteId);

    /**
     * Elimina un cliente (soft delete - marca como inactivo permanentemente).
     * El registro permanece en la BD para auditoría e historial.
     *
     * Acciones:
     * - Marca cliente como INACTIVO
     * - Persiste cambios
     * - Publica evento ClienteEliminado
     * - Notifica a ms-cuentas para inactivar/eliminar cuentas asociadas
     *
     * @param clienteId ID del cliente a eliminar
     * @throws com.devsu.clientes.domain.exception.ClienteNoEncontradoException si cliente no existe
     */
    void eliminarCliente(Long clienteId);

    /**
     * Obtiene el número total de clientes en el sistema.
     *
     * @return Cantidad total de clientes
     */
    long contarClientesTotales();

    /**
     * Obtiene el número total de clientes activos.
     *
     * @return Cantidad de clientes activos
     */
    long contarClientesActivos();
}
