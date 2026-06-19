package com.example.service_notificacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service_notificacion.dto.Notificaciondto;
import com.example.service_notificacion.model.Notificacion;
import com.example.service_notificacion.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones relacionadas con las notificaciones")
public class NotificacionController {

    private final NotificacionService service;

    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todas las notificaciones")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar notificación por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscar(
            @Parameter(description = "ID de la notificación")
            @PathVariable Long id) {

        return ResponseEntity.ok(service.buscar(id));
    }

    @Operation(summary = "Buscar notificaciones de un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificaciones encontradas"),
        @ApiResponse(responseCode = "404", description = "Cliente sin notificaciones")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Notificacion>> buscarCliente(

            @Parameter(description = "ID del cliente")
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                service.buscarPorCliente(clienteId));
    }

    @Operation(summary = "Enviar una notificación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación enviada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/enviar")
    public ResponseEntity<Notificacion> enviar(
            @RequestBody Notificaciondto dto) {

        return ResponseEntity.ok(
                service.enviar(dto));
    }

    @Operation(summary = "Marcar una notificación como leída")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación actualizada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}/leido")
    public ResponseEntity<Notificacion> marcarLeido(

            @Parameter(description = "ID de la notificación")
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.marcarLeido(id));
    }

    @Operation(summary = "Eliminar una notificación")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(

            @Parameter(description = "ID de la notificación")
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}