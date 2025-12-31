package io.github.ahumadamob.plangastos.mapper;

import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaResumenDto;
import io.github.ahumadamob.plangastos.dto.TransaccionRequestDto;
import io.github.ahumadamob.plangastos.dto.TransaccionResponseDto;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Transaccion;

@Component
public class TransaccionMapper {

    private final MapperHelper mapperHelper;

    public TransaccionMapper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    public Transaccion requestToEntity(TransaccionRequestDto request) {
        Transaccion transaccion = new Transaccion();
        transaccion.setPresupuesto(mapperHelper.getPresupuesto(request.getPresupuesto_id()));
        transaccion.setRubro(mapperHelper.getRubro(request.getRubro_id()));
        transaccion.setDescripcion(request.getDescripcion());
        transaccion.setCuenta(mapperHelper.getCuentaFinanciera(request.getCuentaFinanciera_id()));
        transaccion.setFecha(request.getFecha());
        transaccion.setMonto(request.getMonto());
        transaccion.setReferenciaExterna(request.getReferenciaExterna());
        transaccion.setPartidaPlanificada(mapperHelper.getPartidaPlanificada(request.getPartidaPlanificada_id()));
        return transaccion;
    }

    public TransaccionResponseDto entityToResponse(Transaccion transaccion) {
        TransaccionResponseDto response = new TransaccionResponseDto();
        response.setId(transaccion.getId());
        response.setPresupuesto(transaccion.getPresupuesto());
        response.setRubro(transaccion.getRubro());
        response.setDescripcion(transaccion.getDescripcion());
        response.setCuenta(transaccion.getCuenta());
        response.setFecha(transaccion.getFecha());
        response.setMonto(transaccion.getMonto());
        response.setReferenciaExterna(transaccion.getReferenciaExterna());
        response.setPartidaPlanificada(mapPartidaPlanificada(transaccion.getPartidaPlanificada()));
        response.setCreatedAt(transaccion.getCreatedAt());
        response.setUpdatedAt(transaccion.getUpdatedAt());
        return response;
    }

    private PartidaPlanificadaResumenDto mapPartidaPlanificada(PartidaPlanificada partidaPlanificada) {
        if (partidaPlanificada == null) {
            return null;
        }
        PartidaPlanificadaResumenDto dto = new PartidaPlanificadaResumenDto();
        dto.setId(partidaPlanificada.getId());
        dto.setPresupuesto(partidaPlanificada.getPresupuesto());
        dto.setRubro(partidaPlanificada.getRubro());
        dto.setDescripcion(partidaPlanificada.getDescripcion());
        dto.setMontoComprometido(partidaPlanificada.getMontoComprometido());
        dto.setFechaObjetivo(partidaPlanificada.getFechaObjetivo());
        dto.setCuota(partidaPlanificada.getCuota());
        dto.setCreatedAt(partidaPlanificada.getCreatedAt());
        dto.setUpdatedAt(partidaPlanificada.getUpdatedAt());
        return dto;
    }
}
