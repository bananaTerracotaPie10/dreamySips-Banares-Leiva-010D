package com.dreamysips.service_resenas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_resenas.model.Resena;
import java.time.LocalDateTime;


@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long>
{

    List<Resena> findByRunCliente (Long runCliente);

    List<Resena> findByIdProducto (Long idProducto);

    List<Resena> findByIdPedido (Long idPedido);

    List<Resena> findByPuntuacion (int puntuacion);

    List<Resena> findByFecha(LocalDateTime fecha);

}
