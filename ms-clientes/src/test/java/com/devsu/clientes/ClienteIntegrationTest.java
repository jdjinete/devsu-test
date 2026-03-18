package com.devsu.clientes;

import com.devsu.clientes.infrastructure.adapters.in.web.dto.ClienteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.mock.mockito.MockBean;
import com.devsu.clientes.infrastructure.adapters.out.event.RabbitMQClienteEventPublisher;

/**
 * Prueba de Integración (F6): Flujo completo de creación de Cliente.
 * Usa Testcontainers para levantar una base de datos PostgreSQL real.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ClienteIntegrationTest {

    @MockBean
    private RabbitMQClienteEventPublisher eventPublisher;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear un nuevo cliente exitosamente y retornar 201 Created")
    void testCrearClienteExitoso() throws Exception {
        // Preparar Datos (Simulando el JSON de entrada)
        ClienteRequest request = new ClienteRequest();
        request.setNombre("Prueba Integracion");
        request.setGenero("M");
        request.setEdad(30);
        request.setIdentificacion("TEST-1234");
        request.setDireccion("Calle Falsa 123");
        request.setTelefono("555-1234");
        request.setContrasena("password123");

        // Ejecutar POST /api/clientes
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Prueba Integracion")))
                .andExpect(jsonPath("$.identificacion", is("TEST-1234")))
                .andExpect(jsonPath("$.estadoCliente", is("ACTIVO")));
    }

    @Test
    @DisplayName("Debe fallar al crear un cliente con identificación duplicada (Validación JPA)")
    void testCrearClienteDuplicado() throws Exception {
        // 1. Crear el primer cliente
        ClienteRequest request = new ClienteRequest();
        request.setNombre("Primero");
        request.setGenero("M");
        request.setEdad(25);
        request.setIdentificacion("UNIQUE-001");
        request.setDireccion("Dir");
        request.setTelefono("123");
        request.setContrasena("secure123");

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // 2. Intentar crear otro con la misma identificación
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
