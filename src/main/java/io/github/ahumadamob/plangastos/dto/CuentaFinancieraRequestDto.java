package io.github.ahumadamob.plangastos.dto;
import java.math.BigDecimal;

import io.github.ahumadamob.plangastos.entity.TipoCuenta;

public class CuentaFinancieraRequestDto {

    private Long usuario_id;
    private Long divisa_id;
    private String nombre;
    private TipoCuenta tipo;
    private BigDecimal saldoInicial;
    private Boolean activo;
    
	public Long getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}
	public Long getDivisa_id() {
		return divisa_id;
	}
	public void setDivisa_id(Long divisa_id) {
		this.divisa_id = divisa_id;
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
