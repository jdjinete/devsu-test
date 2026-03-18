package com.devsu.cuentas.infrastructure.adapters.in.web.mapper;

import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.domain.entity.Movimiento;
import com.devsu.cuentas.infrastructure.adapters.in.web.dto.MovimientoRequest;
import com.devsu.cuentas.infrastructure.adapters.in.web.dto.MovimientoResponse;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

    public Movimiento toDomain(MovimientoRequest request) {
        if (request == null) return null;
        
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(request.tipoMovimiento());
        movimiento.setValor(request.valor());
        
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.numeroCuenta());
        movimiento.setCuenta(cuenta);
        
        return movimiento;
    }

    public MovimientoResponse toResponse(Movimiento domain) {
        if (domain == null) return null;
        
        return new MovimientoResponse(
            domain.getId(),
            domain.getFecha(),
            domain.getTipoMovimiento(),
            domain.getValor(),
            domain.getSaldo(),
            domain.getCuenta() != null ? domain.getCuenta().getNumeroCuenta() : null
        );
    }
}
