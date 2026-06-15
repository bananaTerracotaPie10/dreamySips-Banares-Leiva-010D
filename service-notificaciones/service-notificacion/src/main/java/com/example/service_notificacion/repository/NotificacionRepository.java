package com.example.service_notificacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.service_notificacion.model.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long>{

    List<Notificacion> findByClienteId(Long clienteId);

    List<Notificacion> findByPedidoId(Long pedidoId);
}