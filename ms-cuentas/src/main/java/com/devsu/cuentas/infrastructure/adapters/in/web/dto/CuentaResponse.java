package com.devsu.cuentas.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;

public record CuentaResponse(
    Long id,
    String numeroCuenta,
    String tipoCuenta,
    BigDecimal saldoInicial,
    BigDecimal saldoActual,
    Boolean estado,
    Long clienteId
) {}
