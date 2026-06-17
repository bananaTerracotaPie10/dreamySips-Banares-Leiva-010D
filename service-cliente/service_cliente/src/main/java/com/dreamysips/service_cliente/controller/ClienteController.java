package com.dreamysips.service_cliente.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamysips.service_cliente.model.Cliente;
import com.dreamysips.service_cliente.model.DireccionEnvio;
import com.dreamysips.service_cliente.service.ClienteService;
import com.dreamysips.service_cliente.service.DireccionEnvioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping ("/api/v1/clientes")
@Tag(name = "Clientes", description = "Operaciones de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DireccionEnvioService direccionEnvioService;

    @GetMapping
    public List <Cliente> listarClientes()
    {
        return clienteService.listarTodos();
    }

    @GetMapping ("/{run}")
    public  ResponseEntity <Cliente> obtener(@PathVariable Long run)
    {
        return clienteService.buscarPorRun(run)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //CRUD

    @PostMapping
    public ResponseEntity <Cliente> crear (@RequestBody Cliente cliente)
    {
        return  ResponseEntity.ok(clienteService.guardar(cliente));
    }

    @DeleteMapping ("/{run}")
    public ResponseEntity<Void> eliminar (@PathVariable Long run)
    {
        clienteService.eliminar(run);
        return ResponseEntity.noContent().build();
    }

    // agrega direccion

    @PostMapping("/{run}/direcciones")
    public ResponseEntity<DireccionEnvio> agregarDireccion(
        @PathVariable Long run,
        @RequestBody DireccionEnvio direccion) 
    {
    return ResponseEntity.ok(direccionEnvioService.guardarConCliente(run, direccion));
    }

    // agrega direccion con id de direccion

    @PostMapping("/{run}/direcciones/{idDireccion}")
    public ResponseEntity<DireccionEnvio> vincularDireccion(
    @PathVariable Long run,
    @PathVariable Long idDireccion) 
    {
        return ResponseEntity.ok(direccionEnvioService.vincularConCliente(run, idDireccion));
    }

    // query por nombre y apellido

    @GetMapping("/buscar/nombre/{nombre}")
    public List<Cliente> buscarPorNombre(@PathVariable String nombre) 
    {
        return clienteService.buscarPorNombre(nombre);
    }

    @GetMapping("/buscar/apellido/{apellido}")
    public List<Cliente> buscarPorApellido(@PathVariable String apellido) 
    {
        return clienteService.buscarPorApellido(apellido);
    }

    @GetMapping("/buscar/nombre-parcial/{nombre}")
    public List<Cliente> buscarPorNombreParcial(@PathVariable String nombre) 
    {
        return clienteService.buscarPorNombreParcial(nombre);
    }

    //query por correo

    @GetMapping("/buscar/correo/{correo}")
    public ResponseEntity<Cliente> buscarPorCorreo(@PathVariable String correo) 
    {
        return clienteService.buscarPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/existe/correo/{correo}")
    public ResponseEntity<Boolean> existeCorreo(@PathVariable String correo) 
    {
        return ResponseEntity.ok(clienteService.existeCorreo(correo));
    }

    // comuna o ciudad 
    @GetMapping("/buscar/comuna/{comuna}")
    public List<Cliente> buscarPorComuna(@PathVariable String comuna) 
    {
        return clienteService.buscarPorComuna(comuna);
    }

    @GetMapping("/buscar/ciudad/{ciudad}")
    public List<Cliente> buscarPorCiudad(@PathVariable String ciudad) 
    {
        return clienteService.buscarPorCiudad(ciudad);
    }

    // Con o sin dirección 
    @GetMapping("/con-direccion")
    public List<Cliente> listarConDireccion() 
    {
        return clienteService.listarConDireccion();
    }

    @GetMapping("/sin-direccion")
    public List<Cliente> listarSinDireccion() 
    {
        return clienteService.listarSinDireccion();
    }


}
