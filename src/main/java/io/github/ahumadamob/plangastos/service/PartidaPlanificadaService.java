package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import java.util.List;

public interface PartidaPlanificadaService {

    List<PartidaPlanificada> getAll();

    PartidaPlanificada getById(Long id);

    PartidaPlanificada create(PartidaPlanificada partidaPlanificada);

    PartidaPlanificada update(Long id, PartidaPlanificada partidaPlanificada);

    void delete(Long id);

    List<PartidaPlanificada> getIngresosByPresupuestoId(Long presupuestoId);

    List<PartidaPlanificada> getGastosByPresupuestoId(Long presupuestoId);

    List<PartidaPlanificada> getAhorroByPresupuestoId(Long presupuestoId);
}
