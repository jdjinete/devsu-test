package com.devsu.cuentas.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cuerpo de la petición para la creación de una cuenta bancaria")
public record CuentaRequest(
    
    @Schema(description = "Número de la cuenta bancaria", example = "498715")
    String numeroCuenta,
    
    @Schema(description = "Tipo de cuenta (Ahorros o Corriente)", example = "Ahorros")
    String tipoCuenta,
    
    @Schema(description = "Saldo inicial de la cuenta", example = "2000.00")
    BigDecimal saldoInicial,
    
    @Schema(description = "Estado de la cuenta (true=Activa, false=Inactiva)", example = "true")
    Boolean estado,
    
    @Schema(description = "ID del cliente propietario de la cuenta", example = "1")
    Long clienteId
) {}
