package io.github.ahumadamob.plangastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.ahumadamob.plangastos.entity.Rubro;

@Repository
public interface RubroRepository extends JpaRepository<Rubro, Long> {
}
