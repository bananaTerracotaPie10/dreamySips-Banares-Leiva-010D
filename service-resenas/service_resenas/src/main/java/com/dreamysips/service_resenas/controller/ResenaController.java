package com.dreamysips.service_resenas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamysips.service_resenas.model.Resena;
import com.dreamysips.service_resenas.service.ResenaService;

@RestController
@RequestMapping("api/v1/resenas")
public class ResenaController 
{

    @Autowired
    private ResenaService resenaService;

    @PostMapping
    public Resena crear (@RequestBody Resena resena)
    {
        return resenaService.crearResena(resena);
    }

    @GetMapping
    public List <Resena> listarResenas ()
    {
        return resenaService.listarResenas();
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Resena> buscarPorId(@PathVariable Long id) 
    {
        return resenaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/completo")
    public ResponseEntity<Resena> obtenerCompleto(@PathVariable Long id) 
    {
        return ResponseEntity.ok(resenaService.obtenerResenaCompleta(id));
    }

    @GetMapping ("puntuacion/{puntuacion}")
    public ResponseEntity <List<Resena>> buscarPorPuntuacion(@PathVariable int puntuacion) 
    {
        List<Resena> resenas = resenaService.buscarPorPuntuacion(puntuacion);

        if (resenas.isEmpty()) 
        {
            return ResponseEntity.notFound().build();
        }       

        return ResponseEntity.ok(resenas);
    }



}
