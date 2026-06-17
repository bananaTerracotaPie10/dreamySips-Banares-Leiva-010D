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

import com.dreamysips.service_cliente.model.DireccionEnvio;
import com.dreamysips.service_cliente.service.DireccionEnvioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping ("/api/v1/direcciones")
@Tag(name = "Direcciones de Envio", description = "Operaciones de las Direcciones de Envio")
public class DireccionEnvioController 
{

    @Autowired
    private DireccionEnvioService direccionEnvioService;

    @GetMapping
    public List <DireccionEnvio> listarDirecciones()
    {
        return direccionEnvioService.listarDireccionEnvios();
    }
    
    @GetMapping ("/{id}")
    public ResponseEntity <DireccionEnvio> obtener (@PathVariable Long id)
    {
        return direccionEnvioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Los metodos obtenerPorCiudad y ObtenerPorComuna retornan una direccion

    @GetMapping ("/ciudad/{ciudad}")
    public ResponseEntity <DireccionEnvio> obtenerPorCiudad (@PathVariable String ciudad)
    {
        DireccionEnvio dir = direccionEnvioService.buscarPorCiudad(ciudad);
        return dir != null ? ResponseEntity.ok(dir) : ResponseEntity.notFound().build();
    }

    @GetMapping ("/comuna/{comuna}")
    public ResponseEntity <DireccionEnvio> obtenerPorComuna (@PathVariable String comuna)
    {
        DireccionEnvio dir = direccionEnvioService.buscarPorComuna(comuna);
        return dir != null ? ResponseEntity.ok(dir) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<DireccionEnvio> crear(@RequestBody DireccionEnvio direccion) {
        return ResponseEntity.ok(direccionEnvioService.guardar(direccion));
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity <Void> eliminar (@PathVariable Long id)
    {
        direccionEnvioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    

}