package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.Presupuesto;
import java.util.List;

public interface PresupuestoService {

    List<Presupuesto> getAll();

    List<Presupuesto> getAllOrderByFechaDesdeDesc();

    Presupuesto getById(Long id);

    Presupuesto create(Presupuesto presupuesto);

    Presupuesto update(Long id, Presupuesto presupuesto);

    void delete(Long id);
}
