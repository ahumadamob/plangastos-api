package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.repository.RubroRepository;
import io.github.ahumadamob.plangastos.service.RubroService;
import java.util.List;
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
        return rubroRepository.save(rubro);
    }

    @Override
    public Rubro update(Long id, Rubro rubro) {
        rubro.setId(id);
        return rubroRepository.save(rubro);
    }

    @Override
    public void delete(Long id) {
        rubroRepository.deleteById(id);
    }
}
