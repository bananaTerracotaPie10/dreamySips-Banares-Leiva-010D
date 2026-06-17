package com.dreamysips.service_listadeseos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dreamysips.service_listadeseos.model.ListaDeseos;
import com.dreamysips.service_listadeseos.service.ListaDeseosService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/lista-deseos")
@Tag(name = "Lista de deseos", description = "Operaciones de la Lista de deseos")
public class ListaDeseosController {

    private final ListaDeseosService service;

    public ListaDeseosController(
            ListaDeseosService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ListaDeseos>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaDeseos> buscar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscar(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ListaDeseos>>
            buscarCliente(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                service.buscarPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<ListaDeseos> agregar(
            @RequestParam Long clienteId,
            @RequestParam Long productoId,
            @RequestParam Boolean notificarDescuento) {

        return ResponseEntity.ok(
                service.agregar(
                        clienteId,
                        productoId,
                        notificarDescuento));
    }

    @PostMapping("/verificar-descuento/{productoId}")
    public ResponseEntity<String>
            verificarDescuento(
            @PathVariable Long productoId) {

        service.verificarDescuento(productoId);

        return ResponseEntity.ok(
                "Verificación completada");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}