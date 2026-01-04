package io.github.ahumadamob.plangastos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Schema(name = "PresupuestoRequest")
public class PresupuestoRequestDto {

    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    private String codigo;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    @Schema(name = "presupuestoOrigen_id")
    private Long presupuestoOrigen_id;

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

    public Long getPresupuestoOrigen_id() {
        return presupuestoOrigen_id;
    }

    public void setPresupuestoOrigen_id(Long presupuestoOrigen_id) {
        this.presupuestoOrigen_id = presupuestoOrigen_id;
    }
}
