package com.devsu.cuentas.infrastructure.adapters.in.web;

import com.devsu.cuentas.application.port.in.MovimientoUseCase;
import com.devsu.cuentas.domain.entity.Movimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoUseCase movimientoUseCase;

    @PostMapping
    public ResponseEntity<Movimiento> crear(@RequestBody Movimiento movimiento) {
        return new ResponseEntity<>(movimientoUseCase.registrarMovimiento(movimiento), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Movimiento> listar() {
        return movimientoUseCase.obtenerTodos();
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    public List<Movimiento> porCuenta(@PathVariable String numeroCuenta) {
        return movimientoUseCase.obtenerMovimientosPorCuenta(numeroCuenta);
    }
}
