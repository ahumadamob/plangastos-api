package io.github.ahumadamob.plangastos.dto.mapper;

import io.github.ahumadamob.plangastos.dto.presupuesto.PresupuestoRequestDto;
import io.github.ahumadamob.plangastos.dto.presupuesto.PresupuestoResponseDto;
import io.github.ahumadamob.plangastos.entity.Presupuesto;

public class PresupuestoMapper {

    public static Presupuesto requestToEntity(PresupuestoRequestDto request) {
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setPlan(request.getPlan());
        presupuesto.setNombre(request.getNombre());
        presupuesto.setCodigo(request.getCodigo());
        presupuesto.setFechaDesde(request.getFechaDesde());
        presupuesto.setFechaHasta(request.getFechaHasta());
        presupuesto.setPresupuestoOrigen(request.getPresupuestoOrigen());
        return presupuesto;
    }

    public static PresupuestoResponseDto entityToResponse(Presupuesto presupuesto) {
        PresupuestoResponseDto response = new PresupuestoResponseDto();
        response.setId(presupuesto.getId());
        response.setPlan(presupuesto.getPlan());
        response.setNombre(presupuesto.getNombre());
        response.setCodigo(presupuesto.getCodigo());
        response.setFechaDesde(presupuesto.getFechaDesde());
        response.setFechaHasta(presupuesto.getFechaHasta());
        response.setPresupuestoOrigen(presupuesto.getPresupuestoOrigen());
        response.setCreatedAt(presupuesto.getCreatedAt());
        response.setUpdatedAt(presupuesto.getUpdatedAt());
        return response;
    }
}
