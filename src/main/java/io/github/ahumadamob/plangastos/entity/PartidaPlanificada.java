package io.github.ahumadamob.plangastos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PartidaPlanificada extends RegistroPresupuesto {
    private BigDecimal montoComprometido;
    private LocalDate fechaObjetivo;

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
