package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import java.math.BigDecimal;
import java.util.List;

public interface PartidaPlanificadaService {

    List<PartidaPlanificada> getAllByUsuarioId(Long usuarioId);

    PartidaPlanificada getByIdAndUsuarioId(Long id, Long usuarioId);

    PartidaPlanificada create(PartidaPlanificada partidaPlanificada);

    PartidaPlanificada update(Long id, Long usuarioId, PartidaPlanificada partidaPlanificada);

    void delete(Long id, Long usuarioId);

    List<PartidaPlanificada> getIngresosByPresupuestoIdAndUsuarioId(Long presupuestoId, Long usuarioId);

    List<PartidaPlanificada> getGastosByPresupuestoIdAndUsuarioId(Long presupuestoId, Long usuarioId);

    List<PartidaPlanificada> getAhorroByPresupuestoIdAndUsuarioId(Long presupuestoId, Long usuarioId);

    PartidaPlanificada consolidar(Long id, Long usuarioId);

    PartidaPlanificada actualizarMontoComprometido(Long id, Long usuarioId, BigDecimal montoComprometido, BigDecimal porcentaje);
}
