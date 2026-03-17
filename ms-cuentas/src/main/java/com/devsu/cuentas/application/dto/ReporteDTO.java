package com.devsu.cuentas.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * DTO para el Estado de Cuenta (Requerimiento F4).
 * Ajustado con @JsonProperty para coincidir EXACTAMENTE con el JSON de ejemplo.
 */
public class ReporteDTO {
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("Fecha")
    private LocalDateTime fecha;

    @JsonProperty("Cliente")
    private String cliente;

    @JsonProperty("Numero Cuenta")
    private String numeroCuenta;

    @JsonProperty("Tipo")
    private String tipo;

    @JsonProperty("Saldo Inicial")
    private BigDecimal saldoInicial;

    @JsonProperty("Estado")
    private Boolean estado;

    @JsonProperty("Movimiento")
    private BigDecimal movimiento;

    @JsonProperty("Saldo Disponible")
    private BigDecimal saldoDisponible;

    public ReporteDTO() {}

    public ReporteDTO(LocalDateTime fecha, String cliente, String numeroCuenta, String tipo, 
                      BigDecimal saldoInicial, Boolean estado, BigDecimal movimiento, BigDecimal saldoDisponible) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.movimiento = movimiento;
        this.saldoDisponible = saldoDisponible;
    }

    // Getters y Setters
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public BigDecimal getMovimiento() { return movimiento; }
    public void setMovimiento(BigDecimal movimiento) { this.movimiento = movimiento; }

    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    public void setSaldoDisponible(BigDecimal saldoDisponible) { this.saldoDisponible = saldoDisponible; }
}
