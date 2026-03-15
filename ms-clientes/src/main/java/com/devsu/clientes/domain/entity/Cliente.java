package com.devsu.clientes.domain.entity;

/**
 * Entidad de Dominio: Cliente
 *
 * Representa un cliente bancario. Hereda de Persona y añade funcionalidades
 * específicas del negocio bancario.
 *
 * Esta es una entidad de dominio PURA sin anotaciones de Spring, JPA o Jackson.
 *
 * Atributos adicionales:
 * - clienteId: Identificador único del cliente (diferente de personaId)
 * - contrasena: Contraseña cifrada para autenticación
 * - estadoCliente: Estado del cliente (ACTIVO/INACTIVO/BLOQUEADO)
 * - ultimoAcceso: Timestamp del último acceso al sistema
 *
 * @author Devsu
 * @version 1.0
 */
public class Cliente extends Persona {

    private Long clienteId;
    private String contrasena;
    private String estadoCliente;
    private Long ultimoAcceso;

    // =====================================================================
    // Constructores
    // =====================================================================

    /**
     * Constructor sin argumentos (para JPA/Persistencia)
     * Solo accesible internamente al dominio y a la infraestructura.
     */
    public Cliente() {
        super();
    }

    /**
     * Constructor con parámetros (para crear nuevos clientes)
     *
     * @param nombre         Nombre de la persona
     * @param genero         Género (M/F/O)
     * @param edad           Edad en años
     * @param identificacion Identificación única
     * @param direccion      Dirección
     * @param telefono       Teléfono
     * @param contrasena     Contraseña del cliente (será cifrada en infraestructura)
     */
    public Cliente(String nombre, String genero, Integer edad,
                      String identificacion, String direccion, String telefono,
                      String contrasena) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        validarContrasena(contrasena);
        this.contrasena = contrasena;
        this.estadoCliente = "ACTIVO";
        this.ultimoAcceso = null;
    }

    // =====================================================================
    // Factory Method (Método de Creación Recomendado)
    // =====================================================================

    /**
     * Factory method para crear un nuevo Cliente.
     * Encapsula la lógica de validación y creación.
     *
     * @param nombre         Nombre del cliente
     * @param genero         Género (M/F/O)
     * @param edad           Edad en años
     * @param identificacion Identificación única
     * @param direccion      Dirección
     * @param telefono       Teléfono
     * @param contrasena     Contraseña (será validada)
     * @return Nueva instancia de Cliente validada
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public static Cliente crear(String nombre, String genero, Integer edad,
                                String identificacion, String direccion,
                                String telefono, String contrasena) {
        return new Cliente(nombre, genero, edad, identificacion, direccion, telefono, contrasena);
    }

    // =====================================================================
    // Métodos de Validación (Lógica de Negocio)
    // =====================================================================

    /**
     * Valida la contraseña del cliente.
     * Requisitos:
     * - No nula ni vacía
     * - Mínimo 8 caracteres
     * - Máximo 50 caracteres
     *
     * @param contrasena La contraseña a validar
     * @throws IllegalArgumentException si la contraseña no cumple requisitos
     */
    private static void validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (contrasena.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (contrasena.length() > 50) {
            throw new IllegalArgumentException("La contraseña no puede exceder 50 caracteres");
        }
    }

    /**
     * Verifica si una contraseña coincide con la del cliente.
     * Nota: La comparación real se realizará en la capa de infraestructura
     * con hashing y salt. Este método es solo para la lógica de dominio.
     *
     * @param contrasenaIngresada La contraseña a verificar
     * @return true si las contraseñas coinciden
     */
    public boolean verificarContrasena(String contrasenaIngresada) {
        if (contrasenaIngresada == null || contrasenaIngresada.isEmpty()) {
            return false;
        }
        // En producción, se usar a bcrypt, argon2, etc.
        // Aquí solo comparamos para lógica de dominio
        return this.contrasena.equals(contrasenaIngresada);
    }

    // =====================================================================
    // Métodos de Comportamiento de Negocio
    // =====================================================================

    /**
     * Actualiza la contraseña del cliente.
     * Valida que la nueva contraseña cumpla requisitos.
     *
     * @param nuevaContrasena La nueva contraseña
     * @throws IllegalArgumentException si la contraseña no es válida
     */
    public void cambiarContrasena(String nuevaContrasena) {
        validarContrasena(nuevaContrasena);
        if (nuevaContrasena.equals(this.contrasena)) {
            throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
        }
        this.contrasena = nuevaContrasena;
    }

    /**
     * Registra el último acceso del cliente al sistema.
     * Se ejecuta después de una autenticación exitosa.
     */
    public void registrarUltimoAcceso() {
        this.ultimoAcceso = System.currentTimeMillis();
    }

    /**
     * Activa el cliente (cambia estadoCliente a ACTIVO).
     * Un cliente activo puede realizar operaciones bancarias.
     *
     * @throws IllegalStateException si el cliente ya está activo
     */
    public void activar() {
        if ("ACTIVO".equals(this.estadoCliente)) {
            throw new IllegalStateException("El cliente ya está activo");
        }
        this.estadoCliente = "ACTIVO";
    }

    /**
     * Inactiva el cliente (cambia estadoCliente a INACTIVO).
     * Un cliente inactivo no puede realizar operaciones.
     *
     * @throws IllegalStateException si el cliente ya está inactivo
     */
    public void inactivar() {
        if ("INACTIVO".equals(this.estadoCliente)) {
            throw new IllegalStateException("El cliente ya está inactivo");
        }
        this.estadoCliente = "INACTIVO";
    }

    /**
     * Bloquea el cliente (cambio estadoCliente a BLOQUEADO).
     * Se usa por razones de seguridad (p.ej., múltiples intentos de acceso fallidos).
     *
     * @throws IllegalStateException si el cliente ya está bloqueado
     */
    public void bloquear() {
        if ("BLOQUEADO".equals(this.estadoCliente)) {
            throw new IllegalStateException("El cliente ya está bloqueado");
        }
        this.estadoCliente = "BLOQUEADO";
    }

    /**
     * Desbloquea el cliente (cambia estadoCliente a ACTIVO).
     * Se usa después de resolver incidentes de seguridad.
     *
     * @throws IllegalStateException si el cliente no está bloqueado
     */
    public void desbloquear() {
        if (!"BLOQUEADO".equals(this.estadoCliente)) {
            throw new IllegalStateException("El cliente no está bloqueado");
        }
        this.estadoCliente = "ACTIVO";
    }

    /**
     * Verifica si el cliente está activo.
     *
     * @return true si el estadoCliente es ACTIVO
     */
    public boolean estaActivoCliente() {
        return "ACTIVO".equals(this.estadoCliente);
    }

    /**
     * Verifica si el cliente está bloqueado.
     *
     * @return true si el estadoCliente es BLOQUEADO
     */
    public boolean estaBloqueado() {
        return "BLOQUEADO".equals(this.estadoCliente);
    }

    /**
     * Verifica si el cliente puede realizar operaciones.
     * Solo clientes ACTIVOS pueden operar.
     *
     * @return true si el cliente puede operar
     */
    public boolean puedeoperar() {
        return estaActivoCliente() && estaActiva();
    }

    // =====================================================================
    // Getters (Métodos de Acceso - Solo Lectura)
    // =====================================================================

    public Long getClienteId() {
        return clienteId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getEstadoCliente() {
        return estadoCliente;
    }

    public Long getUltimoAcceso() {
        return ultimoAcceso;
    }

    // =====================================================================
    // Métodos equals y hashCode (Basados en Cliente ID)
    // =====================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;

        // Primero compara por clienteId si existen
        if (clienteId != null && cliente.clienteId != null) {
            return clienteId.equals(cliente.clienteId);
        }

        // Si no, compara por identificación (herencia)
        return identificacion.equals(cliente.identificacion);
    }

    @Override
    public int hashCode() {
        if (clienteId != null) {
            return clienteId.hashCode();
        }
        return identificacion.hashCode();
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "clienteId=" + clienteId +
                ", nombre='" + nombre + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", estadoCliente='" + estadoCliente + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
