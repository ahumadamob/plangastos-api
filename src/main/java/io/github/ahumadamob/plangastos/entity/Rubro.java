package io.github.ahumadamob.plangastos.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "rubros")
public class Rubro extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NaturalezaMovimiento naturaleza;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Rubro parent;

    @NotNull
    @Column(nullable = false)
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

    public void validarJerarquiaSinCiclos() {
        Set<Long> visitados = new HashSet<>();
        if (getId() != null) {
            visitados.add(getId());
        }

        Rubro actual = parent;
        while (actual != null) {
            Long actualId = actual.getId();
            if (actualId != null && !visitados.add(actualId)) {
                throw new IllegalArgumentException("La jerarquía de rubro contiene una autoreferencia o ciclo");
            }
            actual = actual.getParent();
        }
    }
}
