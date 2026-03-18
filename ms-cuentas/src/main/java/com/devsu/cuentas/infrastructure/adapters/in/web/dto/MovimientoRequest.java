package com.devsu.cuentas.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;

public record MovimientoRequest(
    String tipoMovimiento,
    BigDecimal valor,
    String numeroCuenta
) {}
