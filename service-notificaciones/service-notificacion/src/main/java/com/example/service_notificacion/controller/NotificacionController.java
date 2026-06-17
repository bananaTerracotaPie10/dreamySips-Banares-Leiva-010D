package com.example.service_notificacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service_notificacion.dto.Notificaciondto;
import com.example.service_notificacion.model.Notificacion;
import com.example.service_notificacion.service.NotificacionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones de las Notificaciones")
public class NotificacionController {

    private final NotificacionService service;

    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Notificacion>> buscarCliente(@PathVariable Long clienteId) {

        return ResponseEntity.ok(
                service.buscarPorCliente(clienteId));
    }

    @PostMapping("/enviar")
    public ResponseEntity<Notificacion> enviar(@RequestBody Notificaciondto dto) {

        return ResponseEntity.ok(
                service.enviar(dto));
    }

    @PutMapping("/{id}/leido")
    public ResponseEntity<Notificacion> marcarLeido(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.marcarLeido(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}