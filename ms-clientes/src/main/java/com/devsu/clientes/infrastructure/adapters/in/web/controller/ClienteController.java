package com.devsu.clientes.infrastructure.adapters.in.web.controller;

import com.devsu.clientes.application.service.ClienteUseCase;
import com.devsu.clientes.domain.entity.Cliente;
import com.devsu.clientes.infrastructure.adapters.in.web.dto.ChangePasswordRequest;
import com.devsu.clientes.infrastructure.adapters.in.web.dto.ClienteRequest;
import com.devsu.clientes.infrastructure.adapters.in.web.dto.ClienteResponse;
import com.devsu.clientes.infrastructure.adapters.in.web.dto.ClienteUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteUseCase clienteUseCase;

    @PostMapping
    public ResponseEntity<ClienteResponse> crearCliente(@Valid @RequestBody ClienteRequest request) {
        Cliente cliente = clienteUseCase.crearCliente(
                request.getNombre(),
                request.getGenero(),
                request.getEdad(),
                request.getIdentificacion(),
                request.getDireccion(),
                request.getTelefono(),
                request.getContrasena()
        );
        return new ResponseEntity<>(toResponse(cliente), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerCliente(@PathVariable Long id) {
        return clienteUseCase.obtenerCliente(id)
                .map(cliente -> ResponseEntity.ok(toResponse(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> obtenerTodos() {
        List<ClienteResponse> clientes = clienteUseCase.obtenerTodosLosClientes().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateRequest request) {
        Cliente cliente = clienteUseCase.actualizarCliente(
                id,
                request.getNombre(),
                request.getGenero(),
                request.getEdad(),
                request.getDireccion(),
                request.getTelefono()
        );
        return ResponseEntity.ok(toResponse(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteUseCase.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/contrasena")
    public ResponseEntity<Void> cambiarContrasena(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        clienteUseCase.cambiarContrasena(id, request.getNuevaContrasena());
        return ResponseEntity.ok().build();
    }

    private ClienteResponse toResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setClienteId(cliente.getClienteId());
        response.setNombre(cliente.getNombre());
        response.setIdentificacion(cliente.getIdentificacion());
        response.setDireccion(cliente.getDireccion());
        response.setTelefono(cliente.getTelefono());
        response.setEstadoCliente(cliente.getEstadoCliente());
        return response;
    }
}
