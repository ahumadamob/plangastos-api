package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import java.util.List;

public interface CuentaFinancieraService {

    List<CuentaFinanciera> getAll();

    CuentaFinanciera getById(Long id);

    CuentaFinanciera create(CuentaFinanciera cuentaFinanciera);

    CuentaFinanciera update(Long id, CuentaFinanciera cuentaFinanciera);

    void delete(Long id);
}
