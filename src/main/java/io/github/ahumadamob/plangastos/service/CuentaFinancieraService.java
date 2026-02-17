package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.dto.CuentaFinancieraSaldoDto;
import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import java.util.List;

public interface CuentaFinancieraService {

    List<CuentaFinanciera> getAllByUsuarioId(Long usuarioId);

    List<CuentaFinancieraSaldoDto> getSaldosByUsuarioId(Long usuarioId);

    CuentaFinanciera getById(Long id);

    CuentaFinanciera create(CuentaFinanciera cuentaFinanciera);

    CuentaFinanciera update(Long id, CuentaFinanciera cuentaFinanciera);

    void delete(Long id);
}
