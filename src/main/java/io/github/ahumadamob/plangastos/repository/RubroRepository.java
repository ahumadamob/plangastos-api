package io.github.ahumadamob.plangastos.repository;

import io.github.ahumadamob.plangastos.entity.Rubro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RubroRepository extends JpaRepository<Rubro, Long> {

    List<Rubro> findByUsuarioId(Long usuarioId);

    Optional<Rubro> findByIdAndUsuarioId(Long id, Long usuarioId);

    long countByUsuarioId(Long usuarioId);
}

