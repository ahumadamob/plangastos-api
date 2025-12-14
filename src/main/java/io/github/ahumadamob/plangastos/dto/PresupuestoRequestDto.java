package io.github.ahumadamob.plangastos.dto;

import java.time.LocalDate;

import io.github.ahumadamob.plangastos.entity.PlanPresupuestario;
import io.github.ahumadamob.plangastos.entity.Presupuesto;

public class PresupuestoRequestDto {

    private PlanPresupuestario plan;
    private String nombre;
    private String codigo;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Presupuesto presupuestoOrigen;

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
}
