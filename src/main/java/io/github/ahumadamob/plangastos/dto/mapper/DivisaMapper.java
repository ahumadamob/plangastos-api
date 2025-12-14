package io.github.ahumadamob.plangastos.dto.mapper;

import io.github.ahumadamob.plangastos.dto.divisa.DivisaRequestDto;
import io.github.ahumadamob.plangastos.dto.divisa.DivisaResponseDto;
import io.github.ahumadamob.plangastos.entity.Divisa;

public class DivisaMapper {

    public static Divisa requestToEntity(DivisaRequestDto request) {
        Divisa divisa = new Divisa();
        divisa.setCodigo(request.getCodigo());
        divisa.setNombre(request.getNombre());
        divisa.setSimbolo(request.getSimbolo());
        return divisa;
    }

    public static DivisaResponseDto entityToResponse(Divisa divisa) {
        DivisaResponseDto response = new DivisaResponseDto();
        response.setId(divisa.getId());
        response.setCodigo(divisa.getCodigo());
        response.setNombre(divisa.getNombre());
        response.setSimbolo(divisa.getSimbolo());
        response.setCreatedAt(divisa.getCreatedAt());
        response.setUpdatedAt(divisa.getUpdatedAt());
        return response;
    }
}
