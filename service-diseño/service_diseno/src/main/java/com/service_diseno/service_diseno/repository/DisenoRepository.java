package com.service_diseno.service_diseno.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service_diseno.service_diseno.model.Diseno;


@Repository
public interface DisenoRepository extends JpaRepository<Diseno, Long>{

    Diseno findByIdDiseno(Long idDiseno);

}
