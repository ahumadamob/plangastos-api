package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.PlanPresupuestario;
import java.util.List;

public interface PlanPresupuestarioService {

    List<PlanPresupuestario> getAll();

    PlanPresupuestario getById(Long id);

    PlanPresupuestario create(PlanPresupuestario planPresupuestario);

    PlanPresupuestario update(Long id, PlanPresupuestario planPresupuestario);

    void delete(Long id);
}
