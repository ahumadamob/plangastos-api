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

    List<PartidaPlanificada> findByPresupuestoIdAndUsuarioIdAndRubroNaturaleza(
            Long presupuestoId, Long usuarioId, NaturalezaMovimiento naturalezaMovimiento);

    List<PartidaPlanificada> findByPresupuestoId(Long presupuestoId);

    List<PartidaPlanificada> findByUsuarioId(Long usuarioId);

    Optional<PartidaPlanificada> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<PartidaPlanificada> findByPartidaOrigenId(Long partidaOrigenId);

    List<PartidaPlanificada> findByPartidaOrigenIdAndPresupuestoId(Long partidaOrigenId, Long presupuestoId);

    List<PartidaPlanificada> findByPartidaOrigenIdAndFechaObjetivoGreaterThanEqual(
            Long partidaOrigenId, LocalDate fechaObjetivo);

    long countByUsuarioId(Long usuarioId);
}

