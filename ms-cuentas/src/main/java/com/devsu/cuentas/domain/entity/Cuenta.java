package com.devsu.cuentas.domain.entity;

import com.devsu.cuentas.domain.exception.SaldoNoDisponibleException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad de Dominio: Cuenta
 * 
 * Representa una cuenta bancaria. 
 * Esta es una entidad pura sin anotaciones de persistencia o framework.
 */
public class Cuenta {

    private Long id;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private Boolean estado;
    private Long clienteId;

    public Cuenta() {
    }

    public Cuenta(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, Boolean estado, Long clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoInicial;
        this.estado = estado;
        this.clienteId = clienteId;
    }

    // =====================================================================
    // Lógica Rica de Dominio
    // =====================================================================

    /**
     * Aplica un retiro a la cuenta.
     * 
     * @param valor El valor a retirar (positivo o negativo, se valida internamente)
     * @throws SaldoNoDisponibleException si el retiro supera el saldo disponible (F3)
     */
    public void retirar(BigDecimal valor) {
        BigDecimal valorAbsoluto = valor.abs();
        
        if (this.saldoActual.compareTo(valorAbsoluto) < 0) {
            throw new SaldoNoDisponibleException();
        }
        
        this.saldoActual = this.saldoActual.subtract(valorAbsoluto);
    }

    /**
     * Aplica un depósito a la cuenta.
     * 
     * @param valor El valor a depositar
     */
    public void depositar(BigDecimal valor) {
        BigDecimal valorAbsoluto = valor.abs();
        this.saldoActual = this.saldoActual.add(valorAbsoluto);
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

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return Objects.equals(numeroCuenta, cuenta.numeroCuenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroCuenta);
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldoActual=" + saldoActual +
                ", estado=" + estado +
                '}';
    }
}
