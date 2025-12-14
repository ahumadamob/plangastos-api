package io.github.ahumadamob.plangastos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "partidas_planificadas")
public class PartidaPlanificada extends RegistroPresupuesto {

    @NotNull
    @Column(nullable = false, precision = 18, scale = 2)
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
