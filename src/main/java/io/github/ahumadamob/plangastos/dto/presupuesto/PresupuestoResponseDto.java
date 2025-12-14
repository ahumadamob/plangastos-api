package io.github.ahumadamob.plangastos.dto.presupuesto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.github.ahumadamob.plangastos.entity.PlanPresupuestario;
import io.github.ahumadamob.plangastos.entity.Presupuesto;

public class PresupuestoResponseDto {

    private Long id;
    private PlanPresupuestario plan;
    private String nombre;
    private String codigo;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Presupuesto presupuestoOrigen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanPresupuestario getPlan() {
        return plan;
    }

    public void setPlan(PlanPresupuestario plan) {
        this.plan = plan;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Presupuesto getPresupuestoOrigen() {
        return presupuestoOrigen;
    }

    public void setPresupuestoOrigen(Presupuesto presupuestoOrigen) {
        this.presupuestoOrigen = presupuestoOrigen;
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
