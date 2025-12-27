package io.github.ahumadamob.plangastos.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Positive;

public class PartidaPlanificadaRequestDto {

    private Long presupuesto_id;
    private Long rubro_id;
    private String descripcion;
    private BigDecimal montoComprometido;
    private LocalDate fechaObjetivo;
    @Positive
    private Integer cuotas;
    @Positive
    private Integer cantidadCuotas;
    
	public Long getPresupuesto_id() {
		return presupuesto_id;
	}
	public void setPresupuesto_id(Long presupuesto_id) {
		this.presupuesto_id = presupuesto_id;
	}
	public Long getRubro_id() {
		return rubro_id;
	}
	public void setRubro_id(Long rubro_id) {
		this.rubro_id = rubro_id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getMontoComprometido() {
		return montoComprometido;
	}
	public void setMontoComprometido(BigDecimal montoComprometido) {
		this.montoComprometido = montoComprometido;
	}
	public LocalDate getFechaObjetivo() {
		return fechaObjetivo;
	}
	public void setFechaObjetivo(LocalDate fechaObjetivo) {
		this.fechaObjetivo = fechaObjetivo;
	}
	public Integer getCuotas() {
		return cuotas;
	}
	public void setCuotas(Integer cuotas) {
		this.cuotas = cuotas;
	}
	public Integer getCantidadCuotas() {
		return cantidadCuotas;
	}
	public void setCantidadCuotas(Integer cantidadCuotas) {
		this.cantidadCuotas = cantidadCuotas;
	}

    
}
