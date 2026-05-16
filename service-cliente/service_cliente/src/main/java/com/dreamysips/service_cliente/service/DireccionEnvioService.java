package com.dreamysips.service_cliente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamysips.service_cliente.model.Cliente;
import com.dreamysips.service_cliente.model.DireccionEnvio;
import com.dreamysips.service_cliente.repository.DireccionEnvioRepository;

import jakarta.transaction.Transactional;

@Service
public class DireccionEnvioService 
{

    @Autowired
    private DireccionEnvioRepository direccionEnvioRepository;

    @Autowired
    private ClienteService clienteRepository;

    public List<DireccionEnvio> listarDireccionEnvios()
    {
        return direccionEnvioRepository.findAll();
    }

    public Optional<DireccionEnvio> buscarPorId(Long id)
    {
        return direccionEnvioRepository.findById(id);
    }

    public DireccionEnvio buscarPorCiudad(String ciudad)
    {
        return direccionEnvioRepository.findByCiudad(ciudad);
    }

    public DireccionEnvio buscarPorComuna (String comuna)
    {
        return direccionEnvioRepository.findByComuna(comuna);
    }

    @Transactional
    public DireccionEnvio guardar (DireccionEnvio direccionEnvio)
    {
        return direccionEnvioRepository.save(direccionEnvio);
    }


    public void eliminar (Long id)
    {
        direccionEnvioRepository.deleteById(id);
    }

    @Transactional
    public DireccionEnvio guardarConCliente(Long run, DireccionEnvio direccion) 
    {
    Cliente cliente = clienteRepository.buscarPorRun(run)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    direccion.setCliente(cliente);
    return direccionEnvioRepository.save(direccion);
    }

    public DireccionEnvio vincularConCliente(Long run, Long idDireccion) {
    Cliente cliente = clienteRepository.buscarPorRun(run)
        .orElseThrow(() -> new RuntimeException("Cliente no encontrado con run: " + run));
    
    DireccionEnvio direccion = direccionEnvioRepository.findById(idDireccion)
        .orElseThrow(() -> new RuntimeException("Dirección no encontrada con id: " + idDireccion));
    
    direccion.setCliente(cliente);
    return direccionEnvioRepository.save(direccion);
}

    //Se creo esta clase para mantener la separacion de responsabilidades
    
    



}
