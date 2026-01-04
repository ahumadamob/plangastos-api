package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Transaccion;
import io.github.ahumadamob.plangastos.repository.TransaccionRepository;
import io.github.ahumadamob.plangastos.service.TransaccionService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransaccionServiceJpa implements TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionServiceJpa(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public List<Transaccion> getAll() {
        return transaccionRepository.findAll();
    }

    @Override
    public Transaccion getById(Long id) {
        return transaccionRepository.findById(id).orElse(null);
    }

    @Override
    public Transaccion create(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion update(Long id, Transaccion transaccion) {
        transaccion.setId(id);
        return transaccionRepository.save(transaccion);
    }

    @Override
    public void delete(Long id) {
        transaccionRepository.deleteById(id);
    }
}
