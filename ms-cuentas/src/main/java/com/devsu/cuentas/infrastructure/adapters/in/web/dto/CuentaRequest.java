package com.devsu.cuentas.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;

public record CuentaRequest(
    String numeroCuenta,
    String tipoCuenta,
    BigDecimal saldoInicial,
    Boolean estado,
    Long clienteId
) {}
