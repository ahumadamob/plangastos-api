package io.github.ahumadamob.plangastos.mapper;

import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.dto.PresupuestoDropdownDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoItemDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoOrigenDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoRequestDto;
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

    public PresupuestoItemDto entityToResponse(Presupuesto presupuesto) {
        PresupuestoItemDto response = new PresupuestoItemDto();
        response.setId(presupuesto.getId());
        response.setNombre(presupuesto.getNombre());
        response.setCodigo(presupuesto.getCodigo());
        response.setFechaDesde(presupuesto.getFechaDesde());
        response.setFechaHasta(presupuesto.getFechaHasta());
        response.setPresupuestoOrigen(toPresupuestoOrigenDto(presupuesto.getPresupuestoOrigen()));
        return response;
    }

    public PresupuestoDropdownDto entityToDropdownDto(Presupuesto presupuesto) {
        PresupuestoDropdownDto dropdownDto = new PresupuestoDropdownDto();
        dropdownDto.setId(presupuesto.getId());
        dropdownDto.setNombre(presupuesto.getNombre());
        return dropdownDto;
    }

    private PresupuestoOrigenDto toPresupuestoOrigenDto(Presupuesto presupuestoOrigen) {
        if (presupuestoOrigen == null) {
            return null;
        }
        PresupuestoOrigenDto dto = new PresupuestoOrigenDto();
        dto.setId(presupuestoOrigen.getId());
        dto.setNombre(presupuestoOrigen.getNombre());
        return dto;
    }
}
