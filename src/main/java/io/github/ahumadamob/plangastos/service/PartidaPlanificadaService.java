package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import java.math.BigDecimal;
import java.util.List;

public interface PartidaPlanificadaService {

    List<PartidaPlanificada> getAllByUsuarioId(Long usuarioId);

    PartidaPlanificada getById(Long id);

    PartidaPlanificada create(PartidaPlanificada partidaPlanificada);

    PartidaPlanificada update(Long id, PartidaPlanificada partidaPlanificada);

    void delete(Long id);

    List<PartidaPlanificada> getIngresosByPresupuestoIdAndUsuarioId(Long presupuestoId, Long usuarioId);

    List<PartidaPlanificada> getGastosByPresupuestoIdAndUsuarioId(Long presupuestoId, Long usuarioId);

    List<PartidaPlanificada> getAhorroByPresupuestoIdAndUsuarioId(Long presupuestoId, Long usuarioId);

    PartidaPlanificada consolidar(Long id);

    PartidaPlanificada actualizarMontoComprometido(Long id, BigDecimal montoComprometido, BigDecimal porcentaje);
}
