package com.devsu.clientes.application.service;

import com.devsu.clientes.domain.entity.Cliente;
import com.devsu.clientes.domain.exception.ClienteNoEncontradoException;
import com.devsu.clientes.domain.exception.ClienteYaExisteException;
import com.devsu.clientes.domain.port.ClienteEventPublisherPort;
import com.devsu.clientes.domain.port.ClienteRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteUseCaseTest {

    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;

    @Mock
    private ClienteEventPublisherPort clienteEventPublisherPort;

    @InjectMocks
    private ClienteUseCaseImpl clienteUseCase;

    @Test
    @DisplayName("crearCliente - Cuando Cliente no Existe - Crea y Publica Evento")
    void crearCliente_CuandoNoExiste_CreaYPublicaEvento() {
        // Arrange
        String identificacion = "123456789";
        String nombre = "Juan Perez";
        when(clienteRepositoryPort.existePorIdentificacion(identificacion)).thenReturn(false);
        when(clienteRepositoryPort.guardar(any(Cliente.class))).thenReturn(1L);
        
        Cliente mockCliente = Cliente.crear(nombre, "M", 30, identificacion, "Calle 1", "555-1234", "password123");
        mockCliente.setClienteId(1L);
        when(clienteRepositoryPort.obtenerPorId(1L)).thenReturn(Optional.of(mockCliente));

        // Act
        Cliente resultado = clienteUseCase.crearCliente(nombre, "M", 30, identificacion, "Calle 1", "555-1234", "password123");

        // Assert
        assertNotNull(resultado);
        assertEquals(identificacion, resultado.getIdentificacion());
        verify(clienteRepositoryPort, times(1)).guardar(any(Cliente.class));
        verify(clienteEventPublisherPort, times(1)).publicarClienteCreado(any(Cliente.class));
    }

    @Test
    @DisplayName("crearCliente - Cuando Identificacion Duplicada - Lanza Excepcion")
    void crearCliente_CuandoIdentificacionDuplicada_LanzaClienteYaExisteException() {
        // Arrange
        String identificacion = "123456789";
        when(clienteRepositoryPort.existePorIdentificacion(identificacion)).thenReturn(true);

        // Act & Assert
        assertThrows(ClienteYaExisteException.class, () -> {
            clienteUseCase.crearCliente("Nombre", "M", 30, identificacion, "Dir", "Tel", "Pass");
        });
        verify(clienteRepositoryPort, never()).guardar(any());
        verify(clienteEventPublisherPort, never()).publicarClienteCreado(any());
    }

    @Test
    @DisplayName("obtenerCliente - Cuando Existe - Retorna Cliente")
    void obtenerCliente_CuandoExiste_RetornaCliente() {
        // Arrange
        Long id = 1L;
        Cliente cliente = Cliente.crear("Juan", "M", 30, "123", "Dir", "Tel", "Pass");
        cliente.setClienteId(id);
        when(clienteRepositoryPort.obtenerPorId(id)).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> resultado = clienteUseCase.obtenerCliente(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getClienteId());
    }

    @Test
    @DisplayName("obtenerCliente - Cuando ID Invalido - Lanza IllegalArgumentException")
    void obtenerCliente_CuandoIdInvalido_LanzaIllegalArgumentException() {
        // Arrange
        Long id = -1L;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            clienteUseCase.obtenerCliente(id);
        });
    }

    @Test
    @DisplayName("actualizarCliente - Cuando Existe - Actualiza y Publica Evento")
    void actualizarCliente_CuandoExiste_ActualizaYPublicaEvento() {
        // Arrange
        Long id = 1L;
        Cliente clienteExistente = Cliente.crear("Juan", "M", 30, "123", "Dir", "Tel", "Pass");
        clienteExistente.setClienteId(id);
        when(clienteRepositoryPort.obtenerPorId(id)).thenReturn(Optional.of(clienteExistente));

        // Act
        Cliente resultado = clienteUseCase.actualizarCliente(id, "Juan Actualizado", null, 31, "Nueva Calle", null);

        // Assert
        assertEquals("Juan Actualizado", resultado.getNombre());
        assertEquals(31, resultado.getEdad());
        assertEquals("Nueva Calle", resultado.getDireccion());
        verify(clienteRepositoryPort, times(1)).actualizar(clienteExistente);
        verify(clienteEventPublisherPort, times(1)).publicarClienteActualizado(clienteExistente);
    }

    @Test
    @DisplayName("actualizarCliente - Cuando no Existe - Lanza Excepcion")
    void actualizarCliente_CuandoNoExiste_LanzaClienteNoEncontradoException() {
        // Arrange
        Long id = 1L;
        when(clienteRepositoryPort.obtenerPorId(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNoEncontradoException.class, () -> {
            clienteUseCase.actualizarCliente(id, "Nombre", "M", 30, "Dir", "Tel");
        });
    }
}
