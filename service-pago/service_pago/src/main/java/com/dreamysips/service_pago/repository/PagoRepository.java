package com.dreamysips.service_pago.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_pago.model.Pago;

@Repository
public interface PagoRepository  extends JpaRepository<Pago, Long>
{

    List<Pago> findByIdPago(Long idPago);

    List<Pago> findByIdPedido(Long idPedido);

    List<Pago> findByEstado(String estado);

    List<Pago> findByFechaPago(LocalDateTime fechaPago);
    

}