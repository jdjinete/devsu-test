package com.devsu.cuentas.infrastructure.adapters.in.web;

import com.devsu.cuentas.application.port.in.CuentaUseCase;
import com.devsu.cuentas.domain.entity.Cuenta;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaUseCase cuentaUseCase;

    @PostMapping
    public ResponseEntity<Cuenta> crear(@RequestBody Cuenta cuenta) {
        return new ResponseEntity<>(cuentaUseCase.crearCuenta(cuenta), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Cuenta> listar() {
        return cuentaUseCase.obtenerTodas();
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> obtener(@PathVariable String numeroCuenta) {
        return cuentaUseCase.obtenerCuentaPorNumero(numeroCuenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> actualizar(@PathVariable Long id, @RequestBody Cuenta cuenta) {
        cuenta.setId(id);
        return ResponseEntity.ok(cuentaUseCase.actualizarCuenta(cuenta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuentaUseCase.eliminarCuenta(id);
        return ResponseEntity.noContent().build();
    }
}
