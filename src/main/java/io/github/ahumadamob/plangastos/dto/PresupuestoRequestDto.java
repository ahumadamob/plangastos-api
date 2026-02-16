package io.github.ahumadamob.plangastos.dto;

import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDate;

public class PresupuestoRequestDto {

    private String nombre;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Boolean inactivo;
    private Long presupuestoOrigen_id;

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

    public Long getPresupuestoOrigen_id() {
        return presupuestoOrigen_id;
    }

    public void setPresupuestoOrigen_id(Long presupuestoOrigen_id) {
        this.presupuestoOrigen_id = presupuestoOrigen_id;
    }

    @AssertTrue(message = "fechaDesde debe ser anterior o igual a fechaHasta")
    public boolean isRangoFechasValido() {
        if (fechaDesde == null || fechaHasta == null) {
            return true;
        }
        return !fechaDesde.isAfter(fechaHasta);
    }
}
