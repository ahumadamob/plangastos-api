package io.github.ahumadamob.plangastos.dto;
import io.github.ahumadamob.plangastos.entity.Divisa;
import io.github.ahumadamob.plangastos.entity.Usuario;

public class PlanPresupuestarioRequestDto {

    private Long usuario_id;
    private Long divisa_id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
	public Long getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}
	public Long getDivisa_id() {
		return divisa_id;
	}
	public void setDivisa_id(Long divisa_id) {
		this.divisa_id = divisa_id;
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
