package io.github.ahumadamob.plangastos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(nullable = false)
    private boolean consolidado = false;

    @Positive
    // Se mapea a la columna "cuota" para mantener la nomenclatura en BD.
    @Column(name = "cuota")
    private Integer cuota;

    @Positive
    private Integer cantidadCuotas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_origen_id")
    private PartidaPlanificada partidaOrigen;

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

    public boolean getConsolidado() {
        return consolidado;
    }

    public void setConsolidado(boolean consolidado) {
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

    public PartidaPlanificada getPartidaOrigen() {
        return partidaOrigen;
    }

    public void setPartidaOrigen(PartidaPlanificada partidaOrigen) {
        this.partidaOrigen = partidaOrigen;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public void validarJerarquiaSinCiclos() {
        Set<Long> visitados = new HashSet<>();
        if (getId() != null) {
            visitados.add(getId());
        }

        PartidaPlanificada actual = partidaOrigen;
        while (actual != null) {
            Long actualId = actual.getId();
            if (actualId != null && !visitados.add(actualId)) {
                throw new IllegalArgumentException("La jerarquía de partida planificada contiene una autoreferencia o ciclo");
            }
            actual = actual.getPartidaOrigen();
        }
    }
}
