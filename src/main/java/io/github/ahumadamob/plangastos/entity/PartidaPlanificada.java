package io.github.ahumadamob.plangastos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "partidas_planificadas")
public class PartidaPlanificada extends RegistroPresupuesto {

    @NotNull
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal montoComprometido;

    private LocalDate fechaObjetivo;

    private Boolean consolidado = Boolean.FALSE;

    @Positive
    // Se mapea a la columna "cuotas" para mantener la nomenclatura en BD.
    @Column(name = "cuotas")
    private Integer cuota;

    @Positive
    private Integer cantidadCuotas;

    @OneToMany(mappedBy = "partidaPlanificada", fetch = FetchType.LAZY)
    private List<Transaccion> transacciones = new ArrayList<>();

    @AssertTrue(message = "cuota no debe ser mayor a cantidadCuotas")
    public boolean isCuotaValida() {
        if (cuota == null || cantidadCuotas == null) {
            return true;
        }
        return cuota <= cantidadCuotas;
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

    public Boolean getConsolidado() {
        return consolidado;
    }

    public void setConsolidado(Boolean consolidado) {
        this.consolidado = consolidado;
    }

    public Integer getCuota() {
        return cuota;
    }

    public void setCuota(Integer cuota) {
        this.cuota = cuota;
    }

    public Integer getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setCantidadCuotas(Integer cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }
}
