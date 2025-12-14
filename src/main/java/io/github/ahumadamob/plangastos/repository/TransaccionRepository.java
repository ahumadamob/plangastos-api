package io.github.ahumadamob.plangastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.Transaccion;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
