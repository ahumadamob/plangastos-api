package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.exception.ResourceNotFoundException;
import io.github.ahumadamob.plangastos.repository.RubroRepository;
import io.github.ahumadamob.plangastos.service.RubroService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class RubroServiceJpa implements RubroService {

    private final RubroRepository rubroRepository;

    public RubroServiceJpa(RubroRepository rubroRepository) {
        this.rubroRepository = rubroRepository;
    }

    @Override
    public List<Rubro> getAllByUsuarioId(Long usuarioId) {
        return rubroRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Rubro getByIdAndUsuarioId(Long id, Long usuarioId) {
        return getByIdOwnedByUsuario(id, usuarioId);
    }

    @Override
    public Rubro create(Rubro rubro) {
        validarJerarquiaSinCiclos(rubro);
        return rubroRepository.save(rubro);
    }

    @Override
    public Rubro update(Long id, Long usuarioId, Rubro rubro) {
        getByIdOwnedByUsuario(id, usuarioId);
        rubro.setId(id);
        validarJerarquiaSinCiclos(rubro);
        return rubroRepository.save(rubro);
    }

    @Override
    public void delete(Long id, Long usuarioId) {
        Rubro rubro = getByIdOwnedByUsuario(id, usuarioId);
        rubroRepository.delete(rubro);
    }

    private Rubro getByIdOwnedByUsuario(Long id, Long usuarioId) {
        return rubroRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Rubro no encontrado con id " + id));
    }

    private void validarJerarquiaSinCiclos(Rubro rubro) {
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
            actual = actualId != null ? rubroRepository.findById(actualId).orElse(actual.getParent()) : actual.getParent();
        }

        rubro.validarJerarquiaSinCiclos();
    }
}
