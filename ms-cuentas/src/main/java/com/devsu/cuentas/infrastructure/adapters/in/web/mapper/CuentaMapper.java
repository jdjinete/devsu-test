package com.devsu.cuentas.infrastructure.adapters.in.web.mapper;

import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.infrastructure.adapters.in.web.dto.CuentaRequest;
import com.devsu.cuentas.infrastructure.adapters.in.web.dto.CuentaResponse;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public Cuenta toDomain(CuentaRequest request) {
        if (request == null) return null;
        
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldoInicial(request.saldoInicial());
        cuenta.setSaldoActual(request.saldoInicial());
        cuenta.setEstado(request.estado());
        cuenta.setClienteId(request.clienteId());
        return cuenta;
    }

    public CuentaResponse toResponse(Cuenta domain) {
        if (domain == null) return null;
        
        return new CuentaResponse(
            domain.getId(),
            domain.getNumeroCuenta(),
            domain.getTipoCuenta(),
            domain.getSaldoInicial(),
            domain.getSaldoActual(),
            domain.getEstado(),
            domain.getClienteId()
        );
    }
}
