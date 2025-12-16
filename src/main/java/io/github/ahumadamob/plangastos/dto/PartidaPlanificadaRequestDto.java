package io.github.ahumadamob.plangastos.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.entity.Rubro;

public class PartidaPlanificadaRequestDto {

    private Long presupuesto_id;
    private Long rubro_id;
    private String descripcion;
    private BigDecimal montoComprometido;
    private LocalDate fechaObjetivo;
    
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

    
}
