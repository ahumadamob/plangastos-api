package io.github.ahumadamob.plangastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;

@Repository
public interface CuentaFinancieraRepository extends JpaRepository<CuentaFinanciera, Long> {
}
