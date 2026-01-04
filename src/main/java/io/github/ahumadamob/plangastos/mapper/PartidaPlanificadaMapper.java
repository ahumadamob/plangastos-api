package io.github.ahumadamob.plangastos.mapper;

import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaRequestDto;
import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaResponseDto;
import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaTransaccionDto;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Transaccion;

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
        partida.setCuota(request.getCuota());
        partida.setCantidadCuotas(request.getCantidadCuotas());
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
        response.setConsolidado(partida.getConsolidado());
        response.setCuota(partida.getCuota());
        response.setCantidadCuotas(partida.getCantidadCuotas());
        response.setTransacciones(partida.getTransacciones().stream().map(this::mapTransaccion).toList());
        response.setCreatedAt(partida.getCreatedAt());
        response.setUpdatedAt(partida.getUpdatedAt());
        return response;
    }

    private PartidaPlanificadaTransaccionDto mapTransaccion(Transaccion transaccion) {
        PartidaPlanificadaTransaccionDto dto = new PartidaPlanificadaTransaccionDto();
        dto.setId(transaccion.getId());
        dto.setPresupuesto(transaccion.getPresupuesto());
        dto.setRubro(transaccion.getRubro());
        dto.setDescripcion(transaccion.getDescripcion());
        dto.setCuenta(transaccion.getCuenta());
        dto.setFecha(transaccion.getFecha());
        dto.setMonto(transaccion.getMonto());
        dto.setReferenciaExterna(transaccion.getReferenciaExterna());
        dto.setCreatedAt(transaccion.getCreatedAt());
        dto.setUpdatedAt(transaccion.getUpdatedAt());
        return dto;
    }
}
