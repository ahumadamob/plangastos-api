package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.PlanPresupuestario;
import io.github.ahumadamob.plangastos.repository.PlanPresupuestarioRepository;
import io.github.ahumadamob.plangastos.service.PlanPresupuestarioService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlanPresupuestarioServiceJpa implements PlanPresupuestarioService {

    private final PlanPresupuestarioRepository planPresupuestarioRepository;

    public PlanPresupuestarioServiceJpa(PlanPresupuestarioRepository planPresupuestarioRepository) {
        this.planPresupuestarioRepository = planPresupuestarioRepository;
    }

    @Override
    public List<PlanPresupuestario> getAll() {
        return planPresupuestarioRepository.findAll();
    }

    @Override
    public PlanPresupuestario getById(Long id) {
        return planPresupuestarioRepository.findById(id).orElse(null);
    }

    @Override
    public PlanPresupuestario create(PlanPresupuestario planPresupuestario) {
        return planPresupuestarioRepository.save(planPresupuestario);
    }

    @Override
    public PlanPresupuestario update(Long id, PlanPresupuestario planPresupuestario) {
        planPresupuestario.setId(id);
        return planPresupuestarioRepository.save(planPresupuestario);
    }

    @Override
    public void delete(Long id) {
        planPresupuestarioRepository.deleteById(id);
    }
}
