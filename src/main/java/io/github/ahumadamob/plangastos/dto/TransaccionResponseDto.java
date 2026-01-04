package io.github.ahumadamob.plangastos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.entity.Rubro;

public class TransaccionResponseDto {

    private Long id;
    private Long presupuesto_id;
    private Long rubro_id;
    private String descripcion;
    private CuentaFinanciera cuenta;
    private LocalDate fecha;
    private BigDecimal monto;
    private String referenciaExterna;
    private PartidaPlanificadaResumenDto partidaPlanificada;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    } 

    public void setId(Long id) {
        this.id = id;
    }

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

    public CuentaFinanciera getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaFinanciera cuenta) {
        this.cuenta = cuenta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public PartidaPlanificadaResumenDto getPartidaPlanificada() {
        return partidaPlanificada;
    }

    public void setPartidaPlanificada(PartidaPlanificadaResumenDto partidaPlanificada) {
        this.partidaPlanificada = partidaPlanificada;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
