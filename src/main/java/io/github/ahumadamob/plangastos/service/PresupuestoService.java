package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.Presupuesto;
import java.util.List;

public interface PresupuestoService {

    List<Presupuesto> getAllByUsuarioId(Long usuarioId);

    List<Presupuesto> getAllByUsuarioIdOrderByFechaDesdeDesc(Long usuarioId);

    Presupuesto getById(Long id);

    Presupuesto create(Presupuesto presupuesto);

    Presupuesto update(Long id, Presupuesto presupuesto);

    void delete(Long id);
}
