package com.service_diseno.service_diseno.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service_diseno.service_diseno.model.Diseno;
import com.service_diseno.service_diseno.repository.DisenoRepository;

@Service
public class DisenoService {

    @Autowired
    private DisenoRepository disenoRepository;

    public List<Diseno> listarTodos () {
        return disenoRepository.findAll();
    }

    public Optional<Diseno> buscarPorId(Long id){
        return disenoRepository.findById(id);
    }

    public Diseno guardar(Diseno diseno){
        //Validar que halla un pedido
        if(diseno.getIdDetalle() == null){
            throw new RuntimeException("El diseño debe tener un detalle asociado");
        }   
        return disenoRepository.save(diseno);
    }

    public void eliminar(Long id){
        disenoRepository.deleteById(id);
    }

}
