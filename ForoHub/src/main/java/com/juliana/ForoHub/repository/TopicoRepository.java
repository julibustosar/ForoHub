package com.juliana.ForoHub.repository;

import com.juliana.ForoHub.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    
    Optional<Topico> findByTituloAndMensaje(String titulo, String mensaje);

    boolean existsByTituloAndMensaje(String titulo, String mensaje);
} 