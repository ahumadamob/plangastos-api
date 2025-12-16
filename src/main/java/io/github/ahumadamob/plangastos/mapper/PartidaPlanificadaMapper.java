package io.github.ahumadamob.plangastos.mapper;

import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaRequestDto;
import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaResponseDto;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;

@Component
public class PartidaPlanificadaMapper {

    private final MapperHelper mapperHelper;

    public PartidaPlanificadaMapper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    public PartidaPlanificada requestToEntity(PartidaPlanificadaRequestDto request) {
        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setPresupuesto(mapperHelper.getPresupuesto(request.getPresupuesto_id()));
        partida.setRubro(mapperHelper.getRubro(request.getRubro_id()));
        partida.setDescripcion(request.getDescripcion());
        partida.setMontoComprometido(request.getMontoComprometido());
        partida.setFechaObjetivo(request.getFechaObjetivo());
        return partida;
    }

    public PartidaPlanificadaResponseDto entityToResponse(PartidaPlanificada partida) {
        PartidaPlanificadaResponseDto response = new PartidaPlanificadaResponseDto();
        response.setId(partida.getId());
        response.setPresupuesto(partida.getPresupuesto());
        response.setRubro(partida.getRubro());
        response.setDescripcion(partida.getDescripcion());
        response.setMontoComprometido(partida.getMontoComprometido());
        response.setFechaObjetivo(partida.getFechaObjetivo());
        response.setCreatedAt(partida.getCreatedAt());
        response.setUpdatedAt(partida.getUpdatedAt());
        return response;
    }
}
