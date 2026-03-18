package com.devsu.cuentas.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
    Long id,
    LocalDateTime fecha,
    String tipoMovimiento,
    BigDecimal valor,
    BigDecimal saldo,
    String numeroCuenta
) {}
