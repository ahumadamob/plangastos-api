package io.github.ahumadamob.plangastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.Divisa;

@Repository
public interface DivisaRepository extends JpaRepository<Divisa, Long> {
}
