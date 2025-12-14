package io.github.ahumadamob.plangastos.mapper;

import io.github.ahumadamob.plangastos.dto.planpresupuestario.PlanPresupuestarioRequestDto;
import io.github.ahumadamob.plangastos.dto.planpresupuestario.PlanPresupuestarioResponseDto;
import io.github.ahumadamob.plangastos.entity.PlanPresupuestario;

public class PlanPresupuestarioMapper {

    public static PlanPresupuestario requestToEntity(PlanPresupuestarioRequestDto request) {
        PlanPresupuestario plan = new PlanPresupuestario();
        plan.setUsuario(request.getUsuario());
        plan.setDivisa(request.getDivisa());
        plan.setNombre(request.getNombre());
        plan.setDescripcion(request.getDescripcion());
        plan.setActivo(request.getActivo());
        return plan;
    }

    public static PlanPresupuestarioResponseDto entityToResponse(PlanPresupuestario plan) {
        PlanPresupuestarioResponseDto response = new PlanPresupuestarioResponseDto();
        response.setId(plan.getId());
        response.setUsuario(plan.getUsuario());
        response.setDivisa(plan.getDivisa());
        response.setNombre(plan.getNombre());
        response.setDescripcion(plan.getDescripcion());
        response.setActivo(plan.getActivo());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        return response;
    }
}
