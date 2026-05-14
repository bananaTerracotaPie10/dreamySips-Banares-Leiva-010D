package com.dreamysips.service_pedidos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_pedidos.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>
{

    //Encuentra un pedido por su ID

    Pedido findByIdPedido (Long idPedido);

    @Query ("SELECT p FROM Pedido p")
    List<Pedido> findAllPedidos();

    //Encuentra pedidos por el RUN del cliente

    Pedido findByRunCliente (Long runCliente);

    @Query ("SELECT p FROM Pedido p WHERE p.runCliente = :runCliente")
    List<Pedido> findPedidosByRunCliente(@Param("runCliente") Long runCliente);

    //Encuentra pedidos por la fecha del pedido

    Pedido findByDate (LocalDateTime fechaPedido);
    @Query ("SELECT p FROM Pedido p WHERE p.fechaPedido = :fechaPedido")
    List<Pedido> findByFechaPedido (@Param("fechaPedido") LocalDateTime fechaPedido);

    //Encuentra pedidos por el estado del pedido

    List <Pedido> findByEstado (String estado);
    @Query ("SELECT p FROM Pedido p WHERE LOWER(p.estado) LIKE LOWER(CONCAT('%', :estado, '%'))")
    List<Pedido> findPedidosByEstado(@Param("estado") String estado);
}
