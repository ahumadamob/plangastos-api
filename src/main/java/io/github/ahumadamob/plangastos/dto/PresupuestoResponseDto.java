package io.github.ahumadamob.plangastos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PresupuestoResponseDto {

    private Long id;
    private String nombre;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Boolean inactivo;
    private Long presupuestoOrigen_id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
