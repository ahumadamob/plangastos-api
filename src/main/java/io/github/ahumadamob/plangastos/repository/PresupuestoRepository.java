package io.github.ahumadamob.plangastos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.Presupuesto;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    List<Presupuesto> findAllByOrderByFechaDesdeDesc();

    List<Presupuesto> findByUsuarioIdAndInactivoFalse(Long usuarioId);

    List<Presupuesto> findByUsuarioIdAndInactivoFalseOrderByFechaDesdeDesc(Long usuarioId);

    Optional<Presupuesto> findByIdAndUsuarioId(Long id, Long usuarioId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Presupuesto p
               set p.actual = false
             where p.usuario.id = :usuarioId
               and (:presupuestoIdExcluido is null or p.id <> :presupuestoIdExcluido)
            """)
    int desactivarActualesDeUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("presupuestoIdExcluido") Long presupuestoIdExcluido);

    long countByUsuarioId(Long usuarioId);
}
