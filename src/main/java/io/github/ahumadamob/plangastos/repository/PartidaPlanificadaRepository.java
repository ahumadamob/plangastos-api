package io.github.ahumadamob.plangastos.repository;

import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaPlanificadaRepository extends JpaRepository<PartidaPlanificada, Long> {

    /**
     * Compatibilidad temporal para tests/IDE que todavía usan la firma anterior sin ownership.
     * Evitar su uso en código nuevo.
     */
    @Deprecated
    List<PartidaPlanificada> findByPresupuestoId(Long presupuestoId);

    /**
     * Compatibilidad temporal para tests/IDE que todavía usan la firma anterior sin ownership.
     * Evitar su uso en código nuevo.
     */
    @Deprecated
    List<PartidaPlanificada> findByPartidaOrigenIdAndFechaObjetivoGreaterThanEqual(
            Long partidaOrigenId, LocalDate fechaObjetivo);

    List<PartidaPlanificada> findByUsuarioId(Long usuarioId);

    Optional<PartidaPlanificada> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<PartidaPlanificada> findByPresupuestoIdAndUsuarioIdAndRubroNaturaleza(
            Long presupuestoId, Long usuarioId, NaturalezaMovimiento naturalezaMovimiento);

    List<PartidaPlanificada> findByPresupuestoIdAndUsuarioId(Long presupuestoId, Long usuarioId);

    List<PartidaPlanificada> findByPartidaOrigenIdAndUsuarioId(Long partidaOrigenId, Long usuarioId);

    List<PartidaPlanificada> findByPartidaOrigenIdAndPresupuestoIdAndUsuarioId(Long partidaOrigenId, Long presupuestoId, Long usuarioId);

    List<PartidaPlanificada> findByPartidaOrigenIdAndUsuarioIdAndFechaObjetivoGreaterThanEqual(
            Long partidaOrigenId, Long usuarioId, LocalDate fechaObjetivo);
}
