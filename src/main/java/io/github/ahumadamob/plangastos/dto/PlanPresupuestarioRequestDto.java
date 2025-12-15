package io.github.ahumadamob.plangastos.dto;
import io.github.ahumadamob.plangastos.entity.Divisa;
import io.github.ahumadamob.plangastos.entity.Usuario;

public class PlanPresupuestarioRequestDto {

    private Usuario usuario;
    private Divisa divisa;
    private String nombre;
    private String descripcion;
    private Boolean activo;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Divisa getDivisa() {
        return divisa;
    }

    public void setDivisa(Divisa divisa) {
        this.divisa = divisa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
