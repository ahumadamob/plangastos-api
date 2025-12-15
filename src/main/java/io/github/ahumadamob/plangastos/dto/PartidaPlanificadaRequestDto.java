package io.github.ahumadamob.plangastos.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.entity.Rubro;

public class PartidaPlanificadaRequestDto {

    private Presupuesto presupuesto;
    private Rubro rubro;
    private String descripcion;
    private BigDecimal montoComprometido;
    private LocalDate fechaObjetivo;

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
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
