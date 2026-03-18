package com.devsu.cuentas.infrastructure.adapters.in.web;

import com.devsu.cuentas.application.port.in.MovimientoUseCase;
import com.devsu.cuentas.domain.entity.Movimiento;
import com.devsu.cuentas.infrastructure.adapters.in.web.dto.MovimientoRequest;
import com.devsu.cuentas.infrastructure.adapters.in.web.dto.MovimientoResponse;
import com.devsu.cuentas.infrastructure.adapters.in.web.mapper.MovimientoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoUseCase movimientoUseCase;
    private final MovimientoMapper movimientoMapper;

    @PostMapping
    public ResponseEntity<MovimientoResponse> crear(@RequestBody MovimientoRequest request) {
        Movimiento movimiento = movimientoMapper.toDomain(request);
        Movimiento movimientoCreado = movimientoUseCase.registrarMovimiento(movimiento);
        return new ResponseEntity<>(movimientoMapper.toResponse(movimientoCreado), HttpStatus.CREATED);
    }

    @GetMapping
    public List<MovimientoResponse> listar() {
        return movimientoUseCase.obtenerTodos().stream()
                .map(movimientoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    public List<MovimientoResponse> porCuenta(@PathVariable String numeroCuenta) {
        return movimientoUseCase.obtenerMovimientosPorCuenta(numeroCuenta).stream()
                .map(movimientoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponse> actualizar(@PathVariable Long id, @RequestBody MovimientoRequest request) {
        Movimiento movimiento = movimientoMapper.toDomain(request);
        movimiento.setId(id);
        Movimiento movimientoActualizado = movimientoUseCase.actualizarMovimiento(movimiento);
        return ResponseEntity.ok(movimientoMapper.toResponse(movimientoActualizado));
    }
}
