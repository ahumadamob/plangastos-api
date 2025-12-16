package io.github.ahumadamob.plangastos.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransaccionRequestDto {

    private Long presupuesto_id;
    private Long rubro_id;
    private String descripcion;
    private Long cuentaFinanciera_id;
    private LocalDate fecha;
    private BigDecimal monto;
    private String referenciaExterna;
    private Long partidaPlanificada_id;
	public Long getPresupuesto_id() {
		return presupuesto_id;
	}
	public void setPresupuesto_id(Long presupuesto_id) {
		this.presupuesto_id = presupuesto_id;
	}
	public Long getRubro_id() {
		return rubro_id;
	}
	public void setRubro_id(Long rubro_id) {
		this.rubro_id = rubro_id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getCuentaFinanciera_id() {
		return cuentaFinanciera_id;
	}
	public void setCuentaFinanciera_id(Long cuentaFinanciera_id) {
		this.cuentaFinanciera_id = cuentaFinanciera_id;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getReferenciaExterna() {
		return referenciaExterna;
	}
	public void setReferenciaExterna(String referenciaExterna) {
		this.referenciaExterna = referenciaExterna;
	}
	public Long getPartidaPlanificada_id() {
		return partidaPlanificada_id;
	}
	public void setPartidaPlanificada_id(Long partidaPlanificada_id) {
		this.partidaPlanificada_id = partidaPlanificada_id;
	}

   
}
