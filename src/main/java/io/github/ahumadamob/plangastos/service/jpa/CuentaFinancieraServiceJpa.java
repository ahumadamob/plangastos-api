package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.dto.CuentaFinancieraSaldoDto;
import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import io.github.ahumadamob.plangastos.exception.ResourceNotFoundException;
import io.github.ahumadamob.plangastos.repository.CuentaFinancieraRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.github.ahumadamob.plangastos.security.CurrentUserService;
import io.github.ahumadamob.plangastos.service.CuentaFinancieraService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CuentaFinancieraServiceJpa implements CuentaFinancieraService {

    private final CuentaFinancieraRepository cuentaFinancieraRepository;
    private final UsuarioRepository usuarioRepository;
    private final CurrentUserService currentUserService;

    public CuentaFinancieraServiceJpa(
            CuentaFinancieraRepository cuentaFinancieraRepository,
            UsuarioRepository usuarioRepository,
            CurrentUserService currentUserService) {
        this.cuentaFinancieraRepository = cuentaFinancieraRepository;
        this.usuarioRepository = usuarioRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<CuentaFinanciera> getAll() {
        Long usuarioId = currentUserService.getCurrentUserId();
        return cuentaFinancieraRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<CuentaFinancieraSaldoDto> getSaldos() {
        Long usuarioId = currentUserService.getCurrentUserId();
        return cuentaFinancieraRepository.findAllSaldosByUsuarioId(usuarioId);
    }

    @Override
    public CuentaFinanciera getById(Long id) {
        Long usuarioId = currentUserService.getCurrentUserId();
        return cuentaFinancieraRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta financiera no encontrada con id " + id));
    }

    @Override
    public CuentaFinanciera create(CuentaFinanciera cuentaFinanciera) {
        Long usuarioId = currentUserService.getCurrentUserId();
        cuentaFinanciera.setUsuario(usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId)));
        return cuentaFinancieraRepository.save(cuentaFinanciera);
    }

    @Override
    public CuentaFinanciera update(Long id, CuentaFinanciera cuentaFinanciera) {
        Long usuarioId = currentUserService.getCurrentUserId();
        CuentaFinanciera existente = cuentaFinancieraRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta financiera no encontrada con id " + id));
        cuentaFinanciera.setId(existente.getId());
        cuentaFinanciera.setUsuario(existente.getUsuario());
        return cuentaFinancieraRepository.save(cuentaFinanciera);
    }

    @Override
    public void delete(Long id) {
        CuentaFinanciera existente = getById(id);
        cuentaFinancieraRepository.deleteById(existente.getId());
    }
}
