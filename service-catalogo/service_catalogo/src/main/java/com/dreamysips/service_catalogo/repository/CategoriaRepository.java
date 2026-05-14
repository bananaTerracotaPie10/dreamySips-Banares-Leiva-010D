package com.dreamysips.service_catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_catalogo.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Categoria findByIdCategoria(Long idCategoria);

}
