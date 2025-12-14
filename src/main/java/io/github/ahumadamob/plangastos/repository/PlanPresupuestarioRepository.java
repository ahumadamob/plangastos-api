package io.github.ahumadamob.plangastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.PlanPresupuestario;

@Repository
public interface PlanPresupuestarioRepository extends JpaRepository<PlanPresupuestario, Long> {
}
