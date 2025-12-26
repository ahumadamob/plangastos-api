package io.github.ahumadamob.plangastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import java.util.List;

@Repository
public interface PartidaPlanificadaRepository extends JpaRepository<PartidaPlanificada, Long> {

    List<PartidaPlanificada> findByPresupuestoIdAndRubroNaturaleza(
            Long presupuestoId, NaturalezaMovimiento naturalezaMovimiento);
}
