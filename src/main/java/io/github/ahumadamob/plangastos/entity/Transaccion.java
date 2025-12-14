package io.github.ahumadamob.plangastos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transacciones")
public class Transaccion extends RegistroPresupuesto {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private CuentaFinanciera cuenta;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    private String referenciaExterna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_planificada_id")
    private PartidaPlanificada partidaPlanificada;

    public CuentaFinanciera getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaFinanciera cuenta) {
        this.cuenta = cuenta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public PartidaPlanificada getPartidaPlanificada() {
        return partidaPlanificada;
    }

    public void setPartidaPlanificada(PartidaPlanificada partidaPlanificada) {
        this.partidaPlanificada = partidaPlanificada;
    }
}
