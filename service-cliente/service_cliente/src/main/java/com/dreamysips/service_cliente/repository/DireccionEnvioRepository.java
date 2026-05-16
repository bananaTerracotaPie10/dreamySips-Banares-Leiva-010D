package com.dreamysips.service_cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_cliente.model.DireccionEnvio;

@Repository
public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long>
{
    DireccionEnvio findByCiudad (String ciudad);

    DireccionEnvio findByComuna (String comuna);

}
