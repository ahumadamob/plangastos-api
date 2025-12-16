package io.github.ahumadamob.plangastos.mapper;

import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.dto.RubroRequestDto;
import io.github.ahumadamob.plangastos.dto.RubroResponseDto;
import io.github.ahumadamob.plangastos.entity.Rubro;

@Component
public class RubroMapper {

    private final MapperHelper mapperHelper;

    public RubroMapper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    public Rubro requestToEntity(RubroRequestDto request) {
        Rubro rubro = new Rubro();
        rubro.setUsuario(mapperHelper.getUsuario(request.getUsuario_id()));
        rubro.setNaturaleza(mapperHelper.getNaturalezaMovimiento(request.getNaturalezaMovimiento_id()));
        rubro.setNombre(request.getNombre());
        rubro.setParent(mapperHelper.getRubro(request.getRubro_id()));
        rubro.setActivo(request.getActivo());
        return rubro;
    }

    public RubroResponseDto entityToResponse(Rubro rubro) {
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
