package io.github.ahumadamob.plangastos.dto.rubro;

import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.entity.Usuario;

public class RubroRequestDto {

    private Usuario usuario;
    private NaturalezaMovimiento naturaleza;
    private String nombre;
    private Rubro parent;
    private Boolean activo;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public NaturalezaMovimiento getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(NaturalezaMovimiento naturaleza) {
        this.naturaleza = naturaleza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rubro getParent() {
        return parent;
    }

    public void setParent(Rubro parent) {
        this.parent = parent;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
