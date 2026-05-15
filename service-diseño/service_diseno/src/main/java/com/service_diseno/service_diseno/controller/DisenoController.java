package com.service_diseno.service_diseno.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service_diseno.service_diseno.dto.DisenoDTO;
import com.service_diseno.service_diseno.model.Diseno;
import com.service_diseno.service_diseno.service.DisenoService;

import jakarta.validation.Valid;

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
    public ResponseEntity<List<Diseno>> listar() {
        return ResponseEntity.ok(disenoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diseno> obtener(@PathVariable Long id) {

        return disenoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Diseno> crear(
            @Valid @RequestBody DisenoDTO dto) {

        Diseno diseno = new Diseno();

        diseno.setTamano(dto.getTamano());
        diseno.setAColor(dto.getAColor());
        diseno.setPrecio(dto.getPrecio());
        diseno.setFoto(dto.getFoto());
        diseno.setDibujo(dto.getDibujo());
        diseno.setIdDetalle(dto.getIdDetalle());

        return ResponseEntity.ok(
                disenoService.guardar(diseno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {

        disenoService.eliminar(id);

        return ResponseEntity.ok("Diseño eliminado");
    }

    @GetMapping("/color")
    public ResponseEntity<List<Diseno>> obtenerDisenosColor() {

        return ResponseEntity.ok(
                disenoService.listarDisenosColor());
    }

    @GetMapping("/detalle/{idDetalle}")
    public ResponseEntity<List<Diseno>> buscarPorDetalle(
            @PathVariable Long idDetalle) {

        return ResponseEntity.ok(
                disenoService.buscarPorDetalle(idDetalle));
    }
}