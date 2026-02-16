package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.exception.ResourceNotFoundException;
import io.github.ahumadamob.plangastos.repository.RubroRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.github.ahumadamob.plangastos.security.CurrentUserService;
import io.github.ahumadamob.plangastos.service.RubroService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class RubroServiceJpa implements RubroService {

    private final RubroRepository rubroRepository;
    private final UsuarioRepository usuarioRepository;
    private final CurrentUserService currentUserService;

    public RubroServiceJpa(
            RubroRepository rubroRepository, UsuarioRepository usuarioRepository, CurrentUserService currentUserService) {
        this.rubroRepository = rubroRepository;
        this.usuarioRepository = usuarioRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<Rubro> getAll() {
        Long usuarioId = currentUserService.getCurrentUserId();
        return rubroRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Rubro getById(Long id) {
        Long usuarioId = currentUserService.getCurrentUserId();
        return rubroRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Rubro no encontrado con id " + id));
    }

    @Override
    public Rubro create(Rubro rubro) {
        Long usuarioId = currentUserService.getCurrentUserId();
        rubro.setUsuario(usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId)));
        validarJerarquiaSinCiclos(rubro, usuarioId);
        return rubroRepository.save(rubro);
    }

    @Override
    public Rubro update(Long id, Rubro rubro) {
        Long usuarioId = currentUserService.getCurrentUserId();
        Rubro existente = getById(id);
        rubro.setId(id);
        rubro.setUsuario(existente.getUsuario());
        validarJerarquiaSinCiclos(rubro, usuarioId);
        return rubroRepository.save(rubro);
    }

    @Override
    public void delete(Long id) {
        Rubro existente = getById(id);
        rubroRepository.deleteById(existente.getId());
    }

    private void validarJerarquiaSinCiclos(Rubro rubro, Long usuarioId) {
        Set<Long> visitados = new HashSet<>();
        if (rubro.getId() != null) {
            visitados.add(rubro.getId());
        }

        Rubro actual = rubro.getParent();
        while (actual != null) {
            Long actualId = actual.getId();
            if (actualId != null && !visitados.add(actualId)) {
                throw new IllegalArgumentException("Se detectó un ciclo en parent de Rubro");
            }
            actual = actualId != null ? rubroRepository.findByIdAndUsuarioId(actualId, usuarioId).orElse(actual.getParent()) : actual.getParent();
        }

        rubro.validarJerarquiaSinCiclos();
    }
}
