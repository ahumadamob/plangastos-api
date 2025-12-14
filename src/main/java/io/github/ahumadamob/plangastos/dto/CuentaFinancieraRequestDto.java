package io.github.ahumadamob.plangastos.dto;

import java.math.BigDecimal;

import io.github.ahumadamob.plangastos.entity.Divisa;
import io.github.ahumadamob.plangastos.entity.TipoCuenta;
import io.github.ahumadamob.plangastos.entity.Usuario;

public class CuentaFinancieraRequestDto {

    private Usuario usuario;
    private Divisa divisa;
    private String nombre;
    private TipoCuenta tipo;
    private BigDecimal saldoInicial;
    private Boolean activo;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Divisa getDivisa() {
        return divisa;
    }

    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoCuenta getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuenta tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
