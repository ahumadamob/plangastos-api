package io.github.ahumadamob.plangastos.repository;

import io.github.ahumadamob.plangastos.dto.CuentaFinancieraSaldoDto;
import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaFinancieraRepository extends JpaRepository<CuentaFinanciera, Long> {

    @Query(
            """
            select new io.github.ahumadamob.plangastos.dto.CuentaFinancieraSaldoDto(
                c.id,
                c.nombre,
                d.codigo,
                c.saldoInicial + coalesce(sum(
                    case
                        when r.naturaleza in (io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento.INGRESO,
                                              io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento.RESERVA_AHORRO)
                            then t.monto
                        when r.naturaleza = io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento.GASTO
                            then -t.monto
                        else 0
                    end
                ), 0)
            )
            from CuentaFinanciera c
            join c.divisa d
            left join Transaccion t on t.cuenta.id = c.id
            left join t.partidaPlanificada pp
            left join pp.rubro r
            left join pp.presupuesto p
            where p.id is null or p.inactivo is null or p.inactivo = false
            group by c.id, c.nombre, d.codigo, c.saldoInicial
            order by c.id
            """)
    List<CuentaFinancieraSaldoDto> findAllSaldos();
}
