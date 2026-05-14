package com.dreamysips.service_pedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_pedidos.model.DetallePedido;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long>
{

    //Encuentra un detalle de pedido por su ID

    DetallePedido findByIdDetalle (Long idDetalle);

    @Query ("SELECT d FROM DetallePedido d")
    List <DetallePedido> findAllDetalles();


}
