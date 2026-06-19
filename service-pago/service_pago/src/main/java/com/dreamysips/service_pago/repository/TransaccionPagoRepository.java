package com.dreamysips.service_pago.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_pago.model.TransaccionPago;

@Repository
public interface TransaccionPagoRepository extends JpaRepository<TransaccionPago, Long>
{

    List<TransaccionPago> findByIdTransaccion(Long idTransaccion);

    List<TransaccionPago> findByTipoTransaccion(String tipoTransaccion);

    List<TransaccionPago> findByEstadoTransaccion(String estadoTransaccion);

    List<TransaccionPago> findByFechaTransaccion(LocalDateTime fechaTransaccion);

}
