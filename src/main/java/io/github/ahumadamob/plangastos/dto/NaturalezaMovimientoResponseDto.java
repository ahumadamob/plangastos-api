package io.github.ahumadamob.plangastos.dto;

/**
 * DTO que expone las opciones de {@link io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento}.
 */
public class NaturalezaMovimientoResponseDto {
    private Long id;
    private String codigo;
    private String descripcion;

    public NaturalezaMovimientoResponseDto() {}

    public NaturalezaMovimientoResponseDto(Long id, String codigo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
