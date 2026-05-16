package com.dreamysips.service_pedidos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_pedidos.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>
{


    List<Pedido> findByRunCliente(Long runCliente);
 
    List<Pedido> findByEstado(String estado);
 
    List<Pedido> findByFechaPedido(LocalDateTime fechaPedido);

}
