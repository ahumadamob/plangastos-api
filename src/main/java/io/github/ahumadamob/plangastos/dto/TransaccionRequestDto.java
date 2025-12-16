package io.github.ahumadamob.plangastos.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.entity.Rubro;

public class TransaccionRequestDto {

    private Presupuesto presupuesto;
    private Rubro rubro;
    private String descripcion;
    private CuentaFinanciera cuenta;
    private LocalDate fecha;
    private BigDecimal monto;
    private String referenciaExterna;
    private PartidaPlanificada partidaPlanificada;

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
