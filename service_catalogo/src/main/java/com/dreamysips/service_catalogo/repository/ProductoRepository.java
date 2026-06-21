package com.dreamysips.service_catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_catalogo.model.Producto;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{

    Producto findByIdProducto(Long idProducto);

}
