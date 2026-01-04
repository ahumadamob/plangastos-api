package io.github.ahumadamob.plangastos.mapper;

import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.dto.PresupuestoDropdownDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoRequestDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoResponseDto;
import io.github.ahumadamob.plangastos.entity.Presupuesto;

@Component
public class PresupuestoMapper {

    private final MapperHelper mapperHelper;

    public PresupuestoMapper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    public Presupuesto requestToEntity(PresupuestoRequestDto request) {
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setNombre(request.getNombre());
        presupuesto.setCodigo(request.getCodigo());
        presupuesto.setFechaDesde(request.getFechaDesde());
        presupuesto.setFechaHasta(request.getFechaHasta());
        presupuesto.setPresupuestoOrigen(mapperHelper.getPresupuesto(request.getPresupuestoOrigen_id()));
        return presupuesto;
    }

    public PresupuestoResponseDto entityToResponse(Presupuesto presupuesto) {
        PresupuestoResponseDto response = new PresupuestoResponseDto();
        response.setId(presupuesto.getId());
        response.setNombre(presupuesto.getNombre());
        response.setCodigo(presupuesto.getCodigo());
        response.setFechaDesde(presupuesto.getFechaDesde());
        response.setFechaHasta(presupuesto.getFechaHasta());
        response.setPresupuestoOrigen_id(presupuesto.getPresupuestoOrigen() == null ? null : presupuesto.getPresupuestoOrigen().getId());
        response.setCreatedAt(presupuesto.getCreatedAt());
        response.setUpdatedAt(presupuesto.getUpdatedAt());
        return response;
    }

    public PresupuestoDropdownDto entityToDropdownDto(Presupuesto presupuesto) {
        PresupuestoDropdownDto dropdownDto = new PresupuestoDropdownDto();
        dropdownDto.setId(presupuesto.getId());
        dropdownDto.setNombre(presupuesto.getNombre());
        return dropdownDto;
    }
}
