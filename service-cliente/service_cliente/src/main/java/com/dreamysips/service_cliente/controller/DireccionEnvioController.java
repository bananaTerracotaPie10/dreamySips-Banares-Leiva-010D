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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping ("/api/v1/direcciones")
@Tag(name = "Direcciones de Envio", description = "Operaciones de las Direcciones de Envio")
public class DireccionEnvioController 
{

    @Autowired
    private DireccionEnvioService direccionEnvioService;

    @Operation(summary = "Listar direcciones")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public List<DireccionEnvio> listarDirecciones() {
        return direccionEnvioService.listarDireccionEnvios();
    }
    
    @Operation(summary = "Buscar dirección por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dirección encontrada"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DireccionEnvio> obtener(
            @Parameter(description = "ID de la dirección")
            @PathVariable Long id) {

        return direccionEnvioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Los metodos obtenerPorCiudad y ObtenerPorComuna retornan una direccion

    @Operation(summary = "Buscar dirección por ciudad")
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<DireccionEnvio> obtenerPorCiudad(
            @Parameter(description = "Ciudad")
            @PathVariable String ciudad) {

        DireccionEnvio dir =
                direccionEnvioService.buscarPorCiudad(ciudad);

        return dir != null
                ? ResponseEntity.ok(dir)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar dirección por comuna")
    @GetMapping("/comuna/{comuna}")
    public ResponseEntity<DireccionEnvio> obtenerPorComuna(
            @Parameter(description = "Comuna")
            @PathVariable String comuna) {

        DireccionEnvio dir =
                direccionEnvioService.buscarPorComuna(comuna);

        return dir != null
                ? ResponseEntity.ok(dir)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear dirección de envío")
    @ApiResponse(responseCode = "200", description = "Dirección creada")
    @PostMapping
    public ResponseEntity<DireccionEnvio> crear(
            @RequestBody DireccionEnvio direccion) {

        return ResponseEntity.ok(
                direccionEnvioService.guardar(direccion));
    }

    @Operation(summary = "Eliminar dirección")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Dirección eliminada"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la dirección")
            @PathVariable Long id) 
    {

        direccionEnvioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    

}