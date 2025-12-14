package io.github.ahumadamob.plangastos.dto.mapper;

import io.github.ahumadamob.plangastos.dto.partidaplanificada.PartidaPlanificadaRequestDto;
import io.github.ahumadamob.plangastos.dto.partidaplanificada.PartidaPlanificadaResponseDto;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;

public class PartidaPlanificadaMapper {

    public static PartidaPlanificada requestToEntity(PartidaPlanificadaRequestDto request) {
        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setPresupuesto(request.getPresupuesto());
        partida.setRubro(request.getRubro());
        partida.setDescripcion(request.getDescripcion());
        partida.setMontoComprometido(request.getMontoComprometido());
        partida.setFechaObjetivo(request.getFechaObjetivo());
        return partida;
    }

    public static PartidaPlanificadaResponseDto entityToResponse(PartidaPlanificada partida) {
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
