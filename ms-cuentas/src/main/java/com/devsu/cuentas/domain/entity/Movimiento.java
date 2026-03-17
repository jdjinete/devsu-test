package com.devsu.cuentas.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de Dominio: Movimiento
 * 
 * Representa una transacción o movimiento bancario (Depósito/Retiro).
 * Esta es una entidad pura sin anotaciones de persistencia o framework.
 */
public class Movimiento {

    private Long id;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private Cuenta cuenta;

    public Movimiento() {
        this.fecha = LocalDateTime.now();
    }

    public Movimiento(String tipoMovimiento, BigDecimal valor, BigDecimal saldo, Cuenta cuenta) {
        this.fecha = LocalDateTime.now();
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.cuenta = cuenta;
    }

    // =====================================================================
    // Getters y Setters
    // =====================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", tipo='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", saldoResultante=" + saldo +
                '}';
    }
}
