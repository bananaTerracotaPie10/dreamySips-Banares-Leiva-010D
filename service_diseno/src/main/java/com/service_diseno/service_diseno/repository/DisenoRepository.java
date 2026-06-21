package com.service_diseno.service_diseno.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service_diseno.service_diseno.model.Diseno;


@Repository
public interface DisenoRepository extends JpaRepository<Diseno, Long>{

    List<Diseno> findByIdDetalle(Long idDetalle);

    @Query("""
        SELECT d
        FROM Diseno d
        WHERE d.aColor = true
    """)
    List<Diseno> buscarDisenosColor();

}
