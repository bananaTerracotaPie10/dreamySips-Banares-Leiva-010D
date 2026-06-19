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

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/resenas")
@Tag(name = "Reseñas", description = "Operaciones de Reseñas")
public class ResenaController 
{

    @Autowired
    private ResenaService resenaService;

    @Operation(summary = "Crear una reseña")
    @ApiResponse(responseCode = "200", description = "Reseña creada correctamente")
    @PostMapping
    public Resena crear(@RequestBody Resena resena){
        return resenaService.crearResena(resena);
    }

    @Operation(summary = "Listar todas las reseñas")
    @GetMapping
    public List<Resena> listarResenas(){
        return resenaService.listarResenas();
    }

    @Operation(summary = "Buscar reseña por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")})
    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(@Parameter(description = "ID de la reseña") @PathVariable Long id){
        return resenaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener reseña completa con información relacionada")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}/completo")
    public ResponseEntity<Resena> obtenerCompleto(@Parameter(description = "ID de la reseña") @PathVariable Long id) {

        return ResponseEntity.ok(resenaService.obtenerResenaCompleta(id));
    }

    @Operation(summary = "Buscar reseñas por puntuación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reseñas encontradas"),
        @ApiResponse(responseCode = "404", description = "No existen reseñas con esa puntuación")
    })
    @GetMapping("/puntuacion/{puntuacion}")
    public ResponseEntity<List<Resena>> buscarPorPuntuacion(
            @Parameter(description = "Puntuación de la reseña")
            @PathVariable int puntuacion) {

        List<Resena> resenas = resenaService.buscarPorPuntuacion(puntuacion);

        if (resenas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resenas);
    }



}
