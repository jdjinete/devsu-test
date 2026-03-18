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

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaUseCase cuentaUseCase;
    private final CuentaMapper cuentaMapper;

    @PostMapping
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
