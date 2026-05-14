package com.service_diseno.service_diseno.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service_diseno.service_diseno.model.Diseno;
import com.service_diseno.service_diseno.service.DisenoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/disenos")
public class DisenoController {

    @Autowired
    private DisenoService disenoService;

    @GetMapping
    public List<Diseno> listar() {
        return disenoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diseno> obtener(@PathVariable Long id) {
        return disenoService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Diseno> crear(@RequestBody Diseno diseno){
        return ResponseEntity.ok(disenoService.guardar(diseno));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        disenoService.eliminar(id);
        return ResponseEntity.ok("Diseño eliminado");
    }

}
