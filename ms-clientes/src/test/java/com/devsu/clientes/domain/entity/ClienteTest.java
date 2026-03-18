package com.devsu.clientes.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas unitarias para la entidad Cliente (F5).
 * Sigue el patrón AAA (Arrange, Act, Assert).
 */
class ClienteTest {

    @Test
    @DisplayName("crear - Cuando datos son válidos - Crea instancia con estado ACTIVO")
    void crear_CuandoDatosValidos_CreaInstanciaCorrectamente() {
        // Arrange
        String nombre = "Jose Lema";
        String genero = "M";
        Integer edad = 30;
        String identificacion = "123456";
        String direccion = "Otavalo sn y principal";
        String telefono = "098254785";
        String contrasena = "12345678";

        // Act
        Cliente cliente = Cliente.crear(nombre, genero, edad, identificacion, direccion, telefono, contrasena);

        // Assert
        assertAll(
            () -> assertEquals(nombre, cliente.getNombre()),
            () -> assertEquals(genero, cliente.getGenero()),
            () -> assertEquals(edad, cliente.getEdad()),
            () -> assertEquals(identificacion, cliente.getIdentificacion()),
            () -> assertEquals(direccion, cliente.getDireccion()),
            () -> assertEquals(telefono, cliente.getTelefono()),
            () -> assertEquals(contrasena, cliente.getContrasena()),
            () -> assertEquals("ACTIVO", cliente.getEstadoCliente()),
            () -> assertTrue(cliente.estaActivoCliente())
        );
    }

    @Test
    @DisplayName("crear - Cuando la contraseña es muy corta - Lanza IllegalArgumentException")
    void crear_CuandoContrasenaCorta_LanzaExcepcion() {
        // Arrange
        String contrasenaCorta = "12345";

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", contrasenaCorta);
        });
        assertEquals("La contraseña debe tener al menos 8 caracteres", ex.getMessage());
    }

    @Test
    @DisplayName("crear - Cuando el género es inválido - Lanza IllegalArgumentException (Heredado de Persona)")
    void crear_CuandoGeneroInvalido_LanzaExcepcion() {
        // Arrange
        String generoInvalido = "X";

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Cliente.crear("Nombre", generoInvalido, 30, "123", "Dir", "Tel", "password123");
        });
        assertTrue(ex.getMessage().contains("El género debe ser M"));
    }

    @Test
    @DisplayName("cambiarContrasena - Cuando es válida y distinta - Actualiza la contraseña")
    void cambiarContrasena_CuandoEsValida_ActualizaCorrectamente() {
        // Arrange
        Cliente cliente = Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", "password123");
        String nuevaContrasena = "nuevaClave123";

        // Act
        cliente.cambiarContrasena(nuevaContrasena);

        // Assert
        assertEquals(nuevaContrasena, cliente.getContrasena());
    }

    @Test
    @DisplayName("cambiarContrasena - Cuando es igual a la actual - Lanza IllegalArgumentException")
    void cambiarContrasena_CuandoEsIgualALaActual_LanzaExcepcion() {
        // Arrange
        String pass = "password123";
        Cliente cliente = Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", pass);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cliente.cambiarContrasena(pass);
        });
        assertEquals("La nueva contraseña debe ser diferente a la actual", ex.getMessage());
    }

    @Test
    @DisplayName("bloquear - Cuando está activo - Cambia estado a BLOQUEADO")
    void bloquear_CuandoEstaActivo_CambiaEstado() {
        // Arrange
        Cliente cliente = Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", "password123");

        // Act
        cliente.bloquear();

        // Assert
        assertEquals("BLOQUEADO", cliente.getEstadoCliente());
        assertTrue(cliente.estaBloqueado());
    }

    @Test
    @DisplayName("puedeoPerar - Cuando Cliente y Persona están activos - Retorna true")
    void puedeOperar_CuandoTodoEstaActivo_RetornaTrue() {
        // Arrange
        Cliente cliente = Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", "password123");

        // Act & Assert
        assertTrue(cliente.puedeoperar());
    }

    @Test
    @DisplayName("puedeoPerar - Cuando el Cliente está BLOQUEADO - Retorna false")
    void puedeOperar_CuandoClienteBloqueado_RetornaFalse() {
        // Arrange
        Cliente cliente = Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", "password123");
        cliente.bloquear();

        // Act & Assert
        assertFalse(cliente.puedeoperar());
    }

    @Test
    @DisplayName("desbloquear - Cuando estaba bloqueado - Vuelve a estado ACTIVO")
    void desbloquear_CuandoEstabaBloqueado_RegresaAActivo() {
        // Arrange
        Cliente cliente = Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", "password123");
        cliente.bloquear();

        // Act
        cliente.desbloquear();

        // Assert
        assertEquals("ACTIVO", cliente.getEstadoCliente());
        assertFalse(cliente.estaBloqueado());
    }

    @Test
    @DisplayName("inactivar - Cuando está activo - Cambia a INACTIVO")
    void inactivar_CuandoEstaActivo_CambiaAInactivo() {
        // Arrange
        Cliente cliente = Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", "password123");

        // Act
        cliente.inactivar();

        // Assert
        assertEquals("INACTIVO", cliente.getEstadoCliente());
        assertFalse(cliente.estaActivoCliente());
    }

    @Test
    @DisplayName("registrarUltimoAcceso - Cuando se llama - Actualiza el timestamp")
    void registrarUltimoAcceso_CuandoSeLlama_ActualizaTimestamp() {
        // Arrange
        Cliente cliente = Cliente.crear("Nombre", "M", 30, "123", "Dir", "Tel", "password123");
        assertNull(cliente.getUltimoAcceso());

        // Act
        cliente.registrarUltimoAcceso();

        // Assert
        assertNotNull(cliente.getUltimoAcceso());
        assertTrue(cliente.getUltimoAcceso() <= System.currentTimeMillis());
    }
}
