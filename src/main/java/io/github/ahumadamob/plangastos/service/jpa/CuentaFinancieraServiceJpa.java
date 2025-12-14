package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import io.github.ahumadamob.plangastos.repository.CuentaFinancieraRepository;
import io.github.ahumadamob.plangastos.service.CuentaFinancieraService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CuentaFinancieraServiceJpa implements CuentaFinancieraService {

    private final CuentaFinancieraRepository cuentaFinancieraRepository;

    public CuentaFinancieraServiceJpa(CuentaFinancieraRepository cuentaFinancieraRepository) {
        this.cuentaFinancieraRepository = cuentaFinancieraRepository;
    }

    @Override
    public List<CuentaFinanciera> getAll() {
        return cuentaFinancieraRepository.findAll();
    }

    @Override
    public CuentaFinanciera getById(Long id) {
        return cuentaFinancieraRepository.findById(id).orElse(null);
    }

    @Override
    public CuentaFinanciera create(CuentaFinanciera cuentaFinanciera) {
        return cuentaFinancieraRepository.save(cuentaFinanciera);
    }

    @Override
    public CuentaFinanciera update(Long id, CuentaFinanciera cuentaFinanciera) {
        cuentaFinanciera.setId(id);
        return cuentaFinancieraRepository.save(cuentaFinanciera);
    }

    @Override
    public void delete(Long id) {
        cuentaFinancieraRepository.deleteById(id);
    }
}
