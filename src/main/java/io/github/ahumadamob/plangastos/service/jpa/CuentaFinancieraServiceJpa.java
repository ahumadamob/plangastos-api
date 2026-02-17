package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.dto.CuentaFinancieraSaldoDto;
import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import io.github.ahumadamob.plangastos.exception.ResourceNotFoundException;
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
    public List<CuentaFinanciera> getAllByUsuarioId(Long usuarioId) {
        return cuentaFinancieraRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<CuentaFinancieraSaldoDto> getSaldosByUsuarioId(Long usuarioId) {
        return cuentaFinancieraRepository.findAllSaldosByUsuarioId(usuarioId);
    }

    @Override
    public CuentaFinanciera getByIdAndUsuarioId(Long id, Long usuarioId) {
        return getByIdOwnedByUsuario(id, usuarioId);
    }

    @Override
    public CuentaFinanciera create(CuentaFinanciera cuentaFinanciera) {
        return cuentaFinancieraRepository.save(cuentaFinanciera);
    }

    @Override
    public CuentaFinanciera update(Long id, Long usuarioId, CuentaFinanciera cuentaFinanciera) {
        getByIdOwnedByUsuario(id, usuarioId);
        cuentaFinanciera.setId(id);
        return cuentaFinancieraRepository.save(cuentaFinanciera);
    }

    @Override
    public void delete(Long id, Long usuarioId) {
        CuentaFinanciera cuentaFinanciera = getByIdOwnedByUsuario(id, usuarioId);
        cuentaFinancieraRepository.delete(cuentaFinanciera);
    }

    private CuentaFinanciera getByIdOwnedByUsuario(Long id, Long usuarioId) {
        return cuentaFinancieraRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta financiera no encontrada con id " + id));
    }
}
