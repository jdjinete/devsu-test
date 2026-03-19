package com.devsu.cuentas.infrastructure.adapters.in.web;

import com.devsu.cuentas.application.port.in.CuentaUseCase;
import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.infrastructure.adapters.in.web.dto.CuentaRequest;
import com.devsu.cuentas.infrastructure.adapters.in.web.dto.CuentaResponse;
import com.devsu.cuentas.infrastructure.adapters.in.web.mapper.CuentaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "Operaciones de creación, consulta y gestión de cuentas bacarias")
public class CuentaController {

    private final CuentaUseCase cuentaUseCase;
    private final CuentaMapper cuentaMapper;

    @PostMapping
    @Operation(summary = "Crear una nueva cuenta", description = "Registra una nueva cuenta a nombre de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente",
                    content = { @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = CuentaResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "La solicitud tiene datos inválidos o incompletos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<CuentaResponse> crear(@RequestBody CuentaRequest request) {
        Cuenta cuenta = cuentaMapper.toDomain(request);
        Cuenta cuentaCreada = cuentaUseCase.crearCuenta(cuenta);
        return new ResponseEntity<>(cuentaMapper.toResponse(cuentaCreada), HttpStatus.CREATED);
    }

    @GetMapping
    public List<CuentaResponse> listar() {
        return cuentaUseCase.obtenerTodas().stream()
                .map(cuentaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaResponse> obtener(@PathVariable String numeroCuenta) {
        return cuentaUseCase.obtenerCuentaPorNumero(numeroCuenta)
                .map(cuentaMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> actualizar(@PathVariable Long id, @RequestBody CuentaRequest request) {
        Cuenta cuenta = cuentaMapper.toDomain(request);
        cuenta.setId(id);
        Cuenta cuentaActualizada = cuentaUseCase.actualizarCuenta(cuenta);
        return ResponseEntity.ok(cuentaMapper.toResponse(cuentaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuentaUseCase.eliminarCuenta(id);
        return ResponseEntity.noContent().build();
    }
}
