package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Divisa;
import io.github.ahumadamob.plangastos.repository.DivisaRepository;
import io.github.ahumadamob.plangastos.service.DivisaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DivisaServiceJpa implements DivisaService {

    private final DivisaRepository divisaRepository;

    public DivisaServiceJpa(DivisaRepository divisaRepository) {
        this.divisaRepository = divisaRepository;
    }

    @Override
    public List<Divisa> getAll() {
        return divisaRepository.findAll();
    }

    @Override
    public Divisa getById(Long id) {
        return divisaRepository.findById(id).orElse(null);
    }

    @Override
    public Divisa create(Divisa divisa) {
        return divisaRepository.save(divisa);
    }

    @Override
    public Divisa update(Long id, Divisa divisa) {
        divisa.setId(id);
        return divisaRepository.save(divisa);
    }

    @Override
    public void delete(Long id) {
        divisaRepository.deleteById(id);
    }
}
