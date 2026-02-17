package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.exception.BusinessValidationException;
import io.github.ahumadamob.plangastos.repository.CuentaFinancieraRepository;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.PresupuestoRepository;
import io.github.ahumadamob.plangastos.repository.RubroRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.github.ahumadamob.plangastos.service.UsuarioService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceJpa implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaFinancieraRepository cuentaFinancieraRepository;
    private final RubroRepository rubroRepository;
    private final PresupuestoRepository presupuestoRepository;
    private final PartidaPlanificadaRepository partidaPlanificadaRepository;

    public UsuarioServiceJpa(
            UsuarioRepository usuarioRepository,
            CuentaFinancieraRepository cuentaFinancieraRepository,
            RubroRepository rubroRepository,
            PresupuestoRepository presupuestoRepository,
            PartidaPlanificadaRepository partidaPlanificadaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaFinancieraRepository = cuentaFinancieraRepository;
        this.rubroRepository = rubroRepository;
        this.presupuestoRepository = presupuestoRepository;
        this.partidaPlanificadaRepository = partidaPlanificadaRepository;
    }

    @Override
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario getByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Usuario create(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Long id, Usuario usuario) {
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Long id) {
        validarSinDatosAsociados(id);
        usuarioRepository.deleteById(id);
    }

    private void validarSinDatosAsociados(Long usuarioId) {
        long cuentas = cuentaFinancieraRepository.countByUsuarioId(usuarioId);
        long rubros = rubroRepository.countByUsuarioId(usuarioId);
        long presupuestos = presupuestoRepository.countByUsuarioId(usuarioId);
        long partidas = partidaPlanificadaRepository.countByUsuarioId(usuarioId);

        if (cuentas + rubros + presupuestos + partidas > 0) {
            throw new BusinessValidationException(
                    "No se puede eliminar el usuario porque tiene datos asociados (cuentas, rubros, presupuestos o partidas planificadas)");
        }
    }
}
