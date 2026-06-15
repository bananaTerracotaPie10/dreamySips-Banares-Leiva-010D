package com.dreamysips.service_listadeseos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dreamysips.service_listadeseos.model.ListaDeseos;

public interface ListaDeseosRepository extends JpaRepository<ListaDeseos, Long>{

    List<ListaDeseos> findByClienteId(Long clienteId);

    List<ListaDeseos> findByProductoId(Long productoId);
}