package io.github.ahumadamob.plangastos.mapper;

import io.github.ahumadamob.plangastos.dto.RubroRequestDto;
import io.github.ahumadamob.plangastos.dto.RubroResponseDto;
import io.github.ahumadamob.plangastos.entity.Rubro;

public class RubroMapper {

    public static Rubro requestToEntity(RubroRequestDto request) {
        Rubro rubro = new Rubro();
        rubro.setUsuario(request.getUsuario());
        rubro.setNaturaleza(request.getNaturaleza());
        rubro.setNombre(request.getNombre());
        rubro.setParent(request.getParent());
        rubro.setActivo(request.getActivo());
        return rubro;
    }

    public static RubroResponseDto entityToResponse(Rubro rubro) {
        RubroResponseDto response = new RubroResponseDto();
        response.setId(rubro.getId());
        response.setUsuario(rubro.getUsuario());
        response.setNaturaleza(rubro.getNaturaleza());
        response.setNombre(rubro.getNombre());
        response.setParent(rubro.getParent());
        response.setActivo(rubro.getActivo());
        response.setCreatedAt(rubro.getCreatedAt());
        response.setUpdatedAt(rubro.getUpdatedAt());
        return response;
    }
}
