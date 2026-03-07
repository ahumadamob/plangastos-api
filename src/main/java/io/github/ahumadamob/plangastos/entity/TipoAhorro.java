package io.github.ahumadamob.plangastos.entity;

public enum TipoAhorro {
    PLAZO_FIJO("Plazo fijo"),
    FONDO_INVERSION("Fondo de inversión");

    private final String descripcion;

    TipoAhorro(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
