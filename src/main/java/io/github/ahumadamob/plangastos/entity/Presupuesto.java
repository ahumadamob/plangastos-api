package io.github.ahumadamob.plangastos.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "presupuestos")
public class Presupuesto extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanPresupuestario plan;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @Column(unique = true)
    private String codigo;

    private LocalDate fechaDesde;

    private LocalDate fechaHasta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presupuesto_origen_id")
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
