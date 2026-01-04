package io.github.ahumadamob.plangastos.mapper;

import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.dto.CuentaFinancieraRequestDto;
import io.github.ahumadamob.plangastos.dto.CuentaFinancieraResponseDto;
import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;

@Component
public class CuentaFinancieraMapper {

    private final MapperHelper mapperHelper;

    public CuentaFinancieraMapper(MapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    public CuentaFinanciera requestToEntity(CuentaFinancieraRequestDto request) {
        CuentaFinanciera cuentaFinanciera = new CuentaFinanciera();

        cuentaFinanciera.setUsuario(mapperHelper.getUsuario(request.getUsuario_id()));
        cuentaFinanciera.setDivisa(mapperHelper.getDivisa(request.getDivisa_id()));
        cuentaFinanciera.setNombre(request.getNombre());
        cuentaFinanciera.setTipo(request.getTipo());
        cuentaFinanciera.setSaldoInicial(request.getSaldoInicial());
        cuentaFinanciera.setActivo(request.getActivo());
        return cuentaFinanciera;
    }

    public CuentaFinancieraResponseDto entityToResponse(CuentaFinanciera cuentaFinanciera) {
        CuentaFinancieraResponseDto response = new CuentaFinancieraResponseDto();
        response.setId(cuentaFinanciera.getId());
        response.setUsuario(cuentaFinanciera.getUsuario());
        response.setDivisa(cuentaFinanciera.getDivisa());
        response.setNombre(cuentaFinanciera.getNombre());
        response.setTipo(cuentaFinanciera.getTipo());
        response.setSaldoInicial(cuentaFinanciera.getSaldoInicial());
        response.setActivo(cuentaFinanciera.getActivo());
        response.setCreatedAt(cuentaFinanciera.getCreatedAt());
        response.setUpdatedAt(cuentaFinanciera.getUpdatedAt());
        return response;
    }
}
