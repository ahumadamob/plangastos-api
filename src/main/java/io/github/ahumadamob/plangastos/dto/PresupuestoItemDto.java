package io.github.ahumadamob.plangastos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name = "PresupuestoItem")
public class PresupuestoItemDto {

    private Long id;
    private String nombre;
    private String codigo;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private PresupuestoOrigenDto presupuestoOrigen;

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

    public PresupuestoOrigenDto getPresupuestoOrigen() {
        return presupuestoOrigen;
    }

    public void setPresupuestoOrigen(PresupuestoOrigenDto presupuestoOrigen) {
        this.presupuestoOrigen = presupuestoOrigen;
    }
}
