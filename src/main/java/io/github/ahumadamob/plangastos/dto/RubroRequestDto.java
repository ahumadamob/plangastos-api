package io.github.ahumadamob.plangastos.dto;

public class RubroRequestDto {

    private Long usuario_id;
    private Long naturalezaMovimiento_id;
    private String nombre;
    private Boolean activo;


    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }
    public Long getNaturalezaMovimiento_id() {
        return naturalezaMovimiento_id;
    }

    public void setNaturalezaMovimiento_id(Long naturalezaMovimiento_id) {
        this.naturalezaMovimiento_id = naturalezaMovimiento_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
