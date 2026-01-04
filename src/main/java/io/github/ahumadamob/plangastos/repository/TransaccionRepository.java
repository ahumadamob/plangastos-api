package io.github.ahumadamob.plangastos.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.Transaccion;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    @Query("SELECT COALESCE(SUM(t.monto), 0) FROM Transaccion t WHERE t.partidaPlanificada.id = :partidaPlanificadaId")
    BigDecimal sumMontoByPartidaPlanificadaId(@Param("partidaPlanificadaId") Long partidaPlanificadaId);
}
