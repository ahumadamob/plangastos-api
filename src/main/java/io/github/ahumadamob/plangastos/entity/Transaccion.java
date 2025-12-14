package io.github.ahumadamob.plangastos.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaccion extends RegistroPresupuesto {
    private CuentaFinanciera cuenta;
    private LocalDate fecha;
    private BigDecimal monto;
    private String referenciaExterna;
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
