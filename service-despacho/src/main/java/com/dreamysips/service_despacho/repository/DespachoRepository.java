package com.dreamysips.service_despacho.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dreamysips.service_despacho.model.Despacho;

public interface DespachoRepository extends JpaRepository<Despacho, Long> {

    List<Despacho> findByClienteId(Long clienteId);

    List<Despacho> findByPedidoId(Long pedidoId);

    Optional<Despacho> findByCodigoSeguimiento(String codigoSeguimiento);
}