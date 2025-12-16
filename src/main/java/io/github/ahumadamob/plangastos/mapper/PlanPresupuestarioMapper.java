package io.github.ahumadamob.plangastos.mapper;

import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.dto.PlanPresupuestarioRequestDto;
import io.github.ahumadamob.plangastos.dto.PlanPresupuestarioResponseDto;
import io.github.ahumadamob.plangastos.entity.PlanPresupuestario;

@Component
public class PlanPresupuestarioMapper {

    private final MapperHelper mapperHelper;

    public PlanPresupuestarioMapper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    public PlanPresupuestario requestToEntity(PlanPresupuestarioRequestDto request) {
        PlanPresupuestario plan = new PlanPresupuestario();
        plan.setUsuario(mapperHelper.getUsuario(request.getUsuario_id()));
        plan.setDivisa(mapperHelper.getDivisa(request.getDivisa_id()));
        plan.setNombre(request.getNombre());
        plan.setDescripcion(request.getDescripcion());
        plan.setActivo(request.getActivo());
        return plan;
    }

    public PlanPresupuestarioResponseDto entityToResponse(PlanPresupuestario plan) {
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
