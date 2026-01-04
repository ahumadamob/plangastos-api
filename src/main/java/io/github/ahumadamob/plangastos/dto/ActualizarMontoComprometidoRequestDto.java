package io.github.ahumadamob.plangastos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;

public class ActualizarMontoComprometidoRequestDto {

    private BigDecimal montoComprometido;

    @DecimalMin(value = "-100", message = "El porcentaje mínimo permitido es -100")
    private BigDecimal porcentaje;

    @AssertTrue(message = "Debe especificar montoComprometido o porcentaje, pero no ambos")
    public boolean isSolicitudValida() {
        return (montoComprometido != null) ^ (porcentaje != null);
    }

    public BigDecimal getMontoComprometido() {
        return montoComprometido;
    }

    public void setMontoComprometido(BigDecimal montoComprometido) {
        this.montoComprometido = montoComprometido;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
}
