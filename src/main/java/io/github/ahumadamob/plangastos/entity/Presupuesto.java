package io.github.ahumadamob.plangastos.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "presupuestos")
public class Presupuesto extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    private LocalDate fechaDesde;

    private LocalDate fechaHasta;

    private Boolean inactivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presupuesto_origen_id")
    private Presupuesto presupuestoOrigen;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Boolean getInactivo() {
        return inactivo;
    }

    public void setInactivo(Boolean inactivo) {
        this.inactivo = inactivo;
    }

    public Presupuesto getPresupuestoOrigen() {
        return presupuestoOrigen;
    }

    public void setPresupuestoOrigen(Presupuesto presupuestoOrigen) {
        this.presupuestoOrigen = presupuestoOrigen;
    }

    public void validarJerarquiaSinCiclos() {
        Set<Long> visitados = new HashSet<>();
        if (getId() != null) {
            visitados.add(getId());
        }

        Presupuesto actual = presupuestoOrigen;
        while (actual != null) {
            Long actualId = actual.getId();
            if (actualId != null && !visitados.add(actualId)) {
                throw new IllegalArgumentException("La jerarquía de presupuesto contiene una autoreferencia o ciclo");
            }
            actual = actual.getPresupuestoOrigen();
        }
    }

    @AssertTrue(message = "fechaDesde debe ser anterior o igual a fechaHasta")
    public boolean isRangoFechasValido() {
        if (fechaDesde == null || fechaHasta == null) {
            return true;
        }
        return !fechaDesde.isAfter(fechaHasta);
    }
}
