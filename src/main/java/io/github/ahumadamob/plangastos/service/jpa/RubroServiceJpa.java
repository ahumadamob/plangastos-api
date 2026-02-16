package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Rubro;
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
    public List<Rubro> getAll() {
        return rubroRepository.findAll();
    }

    @Override
    public Rubro getById(Long id) {
        return rubroRepository.findById(id).orElse(null);
    }

    @Override
    public Rubro create(Rubro rubro) {
        validarJerarquiaSinCiclos(rubro);
        return rubroRepository.save(rubro);
    }

    @Override
    public Rubro update(Long id, Rubro rubro) {
        rubro.setId(id);
        validarJerarquiaSinCiclos(rubro);
        return rubroRepository.save(rubro);
    }

    @Override
    public void delete(Long id) {
        rubroRepository.deleteById(id);
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
