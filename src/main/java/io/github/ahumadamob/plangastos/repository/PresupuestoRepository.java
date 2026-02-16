package io.github.ahumadamob.plangastos.repository;

import io.github.ahumadamob.plangastos.entity.Presupuesto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    List<Presupuesto> findByUsuarioIdAndInactivoFalseOrderByFechaDesdeDesc(Long usuarioId);

    List<Presupuesto> findByUsuarioIdAndInactivoFalse(Long usuarioId);

    Optional<Presupuesto> findByIdAndUsuarioId(Long id, Long usuarioId);
}
