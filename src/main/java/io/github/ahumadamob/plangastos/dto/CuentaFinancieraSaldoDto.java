package io.github.ahumadamob.plangastos.dto;

import java.math.BigDecimal;

public class CuentaFinancieraSaldoDto {

    private Long id;
    private String nombre;
    private String divisa;
    private BigDecimal saldo;

    public CuentaFinancieraSaldoDto(Long id, String nombre, String divisa, BigDecimal saldo) {
        this.id = id;
        this.nombre = nombre;
        this.divisa = divisa;
        this.saldo = saldo;
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
}
