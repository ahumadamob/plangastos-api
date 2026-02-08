package io.github.ahumadamob.plangastos.dto;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class PartidaPlanificadaMontoComprometidoRequestDto {

    private BigDecimal montoComprometido;

    @DecimalMin(value = "-100.00", message = "porcentaje no puede ser menor a -100")
    private BigDecimal porcentaje;

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

    public boolean hasMontoComprometido() {
        return montoComprometido != null;
    }

    public boolean hasPorcentaje() {
        return porcentaje != null;
    }
}
