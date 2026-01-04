package io.github.ahumadamob.plangastos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PresupuestoOrigen")
public class PresupuestoOrigenDto {

    private Long id;
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
