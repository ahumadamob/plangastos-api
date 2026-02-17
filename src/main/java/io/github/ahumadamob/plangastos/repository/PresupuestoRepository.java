package io.github.ahumadamob.plangastos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.Presupuesto;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    List<Presupuesto> findAllByOrderByFechaDesdeDesc();

    List<Presupuesto> findByUsuarioIdAndInactivoFalse(Long usuarioId);

    List<Presupuesto> findByUsuarioIdAndInactivoFalseOrderByFechaDesdeDesc(Long usuarioId);

    long countByUsuarioId(Long usuarioId);
}

