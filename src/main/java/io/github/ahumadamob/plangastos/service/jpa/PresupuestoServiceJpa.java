package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.repository.PresupuestoRepository;
import io.github.ahumadamob.plangastos.service.PresupuestoService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PresupuestoServiceJpa implements PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;

    public PresupuestoServiceJpa(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }

    @Override
    public List<Presupuesto> getAll() {
        return presupuestoRepository.findAll();
    }

    @Override
    public Presupuesto getById(Long id) {
        return presupuestoRepository.findById(id).orElse(null);
    }

    @Override
    public Presupuesto create(Presupuesto presupuesto) {
        return presupuestoRepository.save(presupuesto);
    }

    @Override
    public Presupuesto update(Long id, Presupuesto presupuesto) {
        presupuesto.setId(id);
        return presupuestoRepository.save(presupuesto);
    }

    @Override
    public void delete(Long id) {
        presupuestoRepository.deleteById(id);
    }
}
