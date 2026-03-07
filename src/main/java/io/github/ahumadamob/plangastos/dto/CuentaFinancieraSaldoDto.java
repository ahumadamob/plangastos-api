package io.github.ahumadamob.plangastos.dto;

import java.math.BigDecimal;

public class CuentaFinancieraSaldoDto {

    private Long id;
    private String nombre;
    private String divisa;
    private BigDecimal saldo;
    private BigDecimal saldoPlazoFijo;
    private BigDecimal saldoFondoInversion;

    public CuentaFinancieraSaldoDto(
            Long id,
            String nombre,
            String divisa,
            BigDecimal saldo,
            BigDecimal saldoPlazoFijo,
            BigDecimal saldoFondoInversion) {
        this.id = id;
        this.nombre = nombre;
        this.divisa = divisa;
        this.saldo = saldo;
        this.saldoPlazoFijo = saldoPlazoFijo;
        this.saldoFondoInversion = saldoFondoInversion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldoPlazoFijo() {
        return saldoPlazoFijo;
    }

    public void setSaldoPlazoFijo(BigDecimal saldoPlazoFijo) {
        this.saldoPlazoFijo = saldoPlazoFijo;
    }

    public BigDecimal getSaldoFondoInversion() {
        return saldoFondoInversion;
    }

    public void setSaldoFondoInversion(BigDecimal saldoFondoInversion) {
        this.saldoFondoInversion = saldoFondoInversion;
    }
}
