package io.github.ahumadamob.plangastos.dto.mapper;

import io.github.ahumadamob.plangastos.dto.transaccion.TransaccionRequestDto;
import io.github.ahumadamob.plangastos.dto.transaccion.TransaccionResponseDto;
import io.github.ahumadamob.plangastos.entity.Transaccion;

public class TransaccionMapper {

    public static Transaccion requestToEntity(TransaccionRequestDto request) {
        Transaccion transaccion = new Transaccion();
        transaccion.setPresupuesto(request.getPresupuesto());
        transaccion.setRubro(request.getRubro());
        transaccion.setDescripcion(request.getDescripcion());
        transaccion.setCuenta(request.getCuenta());
        transaccion.setFecha(request.getFecha());
        transaccion.setMonto(request.getMonto());
        transaccion.setReferenciaExterna(request.getReferenciaExterna());
        transaccion.setPartidaPlanificada(request.getPartidaPlanificada());
        return transaccion;
    }

    public static TransaccionResponseDto entityToResponse(Transaccion transaccion) {
        TransaccionResponseDto response = new TransaccionResponseDto();
        response.setId(transaccion.getId());
        response.setPresupuesto(transaccion.getPresupuesto());
        response.setRubro(transaccion.getRubro());
        response.setDescripcion(transaccion.getDescripcion());
        response.setCuenta(transaccion.getCuenta());
        response.setFecha(transaccion.getFecha());
        response.setMonto(transaccion.getMonto());
        response.setReferenciaExterna(transaccion.getReferenciaExterna());
        response.setPartidaPlanificada(transaccion.getPartidaPlanificada());
        response.setCreatedAt(transaccion.getCreatedAt());
        response.setUpdatedAt(transaccion.getUpdatedAt());
        return response;
    }
}
