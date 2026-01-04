package io.github.ahumadamob.plangastos.entity;

public enum NaturalezaMovimiento {
    INGRESO("Ingreso"),
    GASTO("Gasto"),
    RESERVA_AHORRO("Reserva/Ahorro");

    private final String descripcion;

    NaturalezaMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
