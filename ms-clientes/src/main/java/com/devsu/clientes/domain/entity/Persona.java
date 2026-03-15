package com.devsu.clientes.domain.entity;

/**
 * Entidad de Dominio: Persona
 *
 * Representa una persona en el sistema. Es la clase base para Cliente.
 * Esta es una entidad de dominio PURA sin anotaciones de Spring, JPA o Jackson.
 *
 * Atributos:
 * - personaId: Identificador único (generado en persistencia)
 * - nombre: Nombre completo de la persona
 * - genero: Género de la persona (M/F/O)
 * - edad: Edad en años
 * - identificacion: Unique identifier (cédula, pasaporte, etc.)
 * - direccion: Dirección física
 * - telefono: Número de teléfono
 * - estado: Estado de la persona (ACTIVA/INACTIVA)
 * - fechaRegistro: Fecha de creación del registro
 *
 * @author Devsu
 * @version 1.0
 */
public class Persona {

    protected Long personaId;
    protected String nombre;
    protected String genero;
    protected Integer edad;
    protected String identificacion;
    protected String direccion;
    protected String telefono;
    protected String estado;
    protected Long fechaRegistro;

    // =====================================================================
    // Constructores
    // =====================================================================

    /**
     * Constructor sin argumentos (para JPA/Persistencia)
     * Solo accesible internamente al dominio y a la infraestructura.
     */
    public Persona() {
    }

    /**
     * Constructor con parámetros (para crear nuevas personas)
     *
     * @param nombre         Nombre de la persona
     * @param genero         Género (M/F/O)
     * @param edad           Edad en años
     * @param identificacion Identificación única
     * @param direccion      Dirección
     * @param telefono       Teléfono
     */
    public Persona(String nombre, String genero, Integer edad,
                      String identificacion, String direccion, String telefono) {
        validarDatos(nombre, genero, edad, identificacion, direccion, telefono);
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = "ACTIVA";
        this.fechaRegistro = System.currentTimeMillis();
    }

    // =====================================================================
    // Factory Method (Método de Creación Recomendado)
    // =====================================================================

    /**
     * Factory method para crear una nueva Persona.
     * Encapsula la lógica de validación y creación.
     *
     * @param nombre         Nombre de la persona
     * @param genero         Género (M/F/O)
     * @param edad           Edad en años
     * @param identificacion Identificación única
     * @param direccion      Dirección
     * @param telefono       Teléfono
     * @return Nueva instancia de Persona validada
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public static Persona crear(String nombre, String genero, Integer edad,
                                String identificacion, String direccion, String telefono) {
        return new Persona(nombre, genero, edad, identificacion, direccion, telefono);
    }

    // =====================================================================
    // Métodos de Validación (Lógica de Negocio)
    // =====================================================================

    /**
     * Valida todos los datos de la persona.
     * Lanza excepción si algún dato es inválido.
     *
     * @throws IllegalArgumentException si algún dato es inválido
     */
    private static void validarDatos(String nombre, String genero, Integer edad,
                                      String identificacion, String direccion, String telefono) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }

        if (genero == null || !genero.matches("[MFO]")) {
            throw new IllegalArgumentException("El género debe ser M (Masculino), F (Femenino) u O (Otro)");
        }

        if (edad == null || edad < 0 || edad > 150) {
            throw new IllegalArgumentException("La edad debe estar entre 0 y 150 años");
        }

        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La identificación no puede estar vacía");
        }
        if (identificacion.length() > 20) {
            throw new IllegalArgumentException("La identificación no puede exceder 20 caracteres");
        }

        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía");
        }
        if (direccion.length() > 200) {
            throw new IllegalArgumentException("La dirección no puede exceder 200 caracteres");
        }

        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }
        if (telefono.length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }
    }

    /**
     * Valida que la identificación sea única en el sistema.
     * Esta validación se realiza a través del puerto de persistencia.
     *
     * @param identificacion La identificación a validar
     * @throws IllegalArgumentException si la identificación ya existe
     */
    protected void validarIdentificacionUnica(String identificacion) {
        // Esta lógica será delegada a la capa de aplicación/infraestructura
        // Esta es solo una marcación de contrato de negocio
    }

    // =====================================================================
    // Métodos de Comportamiento de Negocio
    // =====================================================================

    /**
     * Actualiza los datos de la persona.
     * Valida que los nuevos datos sean válidos.
     *
     * @param nombre     Nuevo nombre
     * @param genero     Nuevo género
     * @param edad       Nueva edad
     * @param direccion  Nueva dirección
     * @param telefono   Nuevo teléfono
     */
    public void actualizar(String nombre, String genero, Integer edad,
                          String direccion, String telefono) {
        validarDatos(nombre, genero, edad, this.identificacion, direccion, telefono);
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    /**
     * Inhabilita la persona (cambia estado a INACTIVA).
     * Una persona inactiva no puede realizar operaciones en sus cuentas.
     */
    public void inhabilitar() {
        if ("INACTIVA".equals(this.estado)) {
            throw new IllegalStateException("La persona ya está inactiva");
        }
        this.estado = "INACTIVA";
    }

    /**
     * Habilita la persona (cambia estado a ACTIVA).
     */
    public void habilitar() {
        if ("ACTIVA".equals(this.estado)) {
            throw new IllegalStateException("La persona ya está activa");
        }
        this.estado = "ACTIVA";
    }

    /**
     * Verifica si la persona está activa.
     *
     * @return true si el estado es ACTIVA
     */
    public boolean estaActiva() {
        return "ACTIVA".equals(this.estado);
    }

    // =====================================================================
    // Getters (Métodos de Acceso - Solo Lectura)
    // =====================================================================

    public Long getPersonaId() {
        return personaId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEstado() {
        return estado;
    }

    public Long getFechaRegistro() {
        return fechaRegistro;
    }
    public void setPersonaId(Long personaId) { this.personaId = personaId; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFechaRegistro(Long fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    // =====================================================================
    // Métodos equals y hashCode (Basados en Identificación)
    // =====================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Persona persona = (Persona) o;

        return identificacion.equals(persona.identificacion);
    }

    @Override
    public int hashCode() {
        return identificacion.hashCode();
    }

    @Override
    public String toString() {
        return "Persona{" +
                "personaId=" + personaId +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", edad=" + edad +
                ", identificacion='" + identificacion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
