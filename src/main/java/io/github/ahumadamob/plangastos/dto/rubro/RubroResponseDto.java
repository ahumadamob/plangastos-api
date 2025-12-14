package io.github.ahumadamob.plangastos.dto.rubro;

import java.time.LocalDateTime;

import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.entity.Usuario;

public class RubroResponseDto {

    private Long id;
    private Usuario usuario;
    private NaturalezaMovimiento naturaleza;
    private String nombre;
    private Rubro parent;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
