package com.dreamysips.service_cliente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamysips.service_cliente.model.Cliente;
import com.dreamysips.service_cliente.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService 
{

    @Autowired
    private ClienteRepository clienteRepository;

    public List <Cliente> listarTodos()
    {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorRun(Long run)
    {
        return clienteRepository.findById(run);
    }

    //CRUD 
    
    @Transactional
    public Cliente guardar (Cliente cliente)
    {
        return clienteRepository.save(cliente);
    }

    public void eliminar (Long run)
    {
        clienteRepository.deleteById(run);
    }

    //query por nombre y apellido

    public List<Cliente> buscarPorNombre(String nombre) 
    {
        return clienteRepository.findByPrimerNombreIgnoreCase(nombre);
    }

    public List<Cliente> buscarPorApellido(String apellido) 
    {
        return clienteRepository.findByApPaternoIgnoreCaseOrApMaternoIgnoreCase(apellido, apellido);
    }

    public List<Cliente> buscarPorNombreParcial(String nombre) 
    {
        return clienteRepository.buscarPorNombreParcial(nombre);
    }

    //query por correo

    public Optional<Cliente> buscarPorCorreo(String correo) 
    {
        return clienteRepository.findByCorreo(correo);
    }

    public boolean existeCorreo(String correo) 
    {
        return clienteRepository.existsByCorreo(correo);
    }

    //query por comuna o ciudad

    public List<Cliente> buscarPorComuna(String comuna) 
    {
        return clienteRepository.findByComuna(comuna);
    }

    public List<Cliente> buscarPorCiudad(String ciudad) 
    {
        return clienteRepository.findByCiudad(ciudad);
    }

    //clientes con o sin direccion

    public List<Cliente> listarConDireccion() 
    {
        return clienteRepository.findClientesConDireccion();
    }

    public List<Cliente> listarSinDireccion() 
    {
        return clienteRepository.findClientesSinDireccion();
    }

}
