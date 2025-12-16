package io.github.ahumadamob.plangastos.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.ahumadamob.plangastos.dto.CuentaFinancieraRequestDto;
import io.github.ahumadamob.plangastos.dto.CuentaFinancieraResponseDto;
import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import io.github.ahumadamob.plangastos.service.CuentaFinancieraService;
import io.github.ahumadamob.plangastos.service.DivisaService;
import io.github.ahumadamob.plangastos.service.UsuarioService;

public class CuentaFinancieraMapper {
	
    public static CuentaFinanciera requestToEntity(CuentaFinancieraRequestDto request) {
        CuentaFinanciera cuentaFinanciera = new CuentaFinanciera();

        cuentaFinanciera.setNombre(request.getNombre());
        cuentaFinanciera.setTipo(request.getTipo());
        cuentaFinanciera.setSaldoInicial(request.getSaldoInicial());
        cuentaFinanciera.setActivo(request.getActivo());
        return cuentaFinanciera;
    }

    public static CuentaFinancieraResponseDto entityToResponse(CuentaFinanciera cuentaFinanciera) {
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
