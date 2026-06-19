package com.dreamysips.service_despacho.controller;

import com.dreamysips.service_despacho.model.Despacho;
import com.dreamysips.service_despacho.service.DespachoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/despachos")
@Tag(name = "Despachos", description = "Gestión de despachos")
public class DespachoController {

    private final DespachoService service;

    public DespachoController(
            DespachoService service) {

        this.service = service;
    }

    @GetMapping
    public List<Despacho> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Despacho buscar(
            @PathVariable Long id) {

        return service.buscar(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Despacho> buscarPorCliente(
            @PathVariable Long clienteId) {

        return service.buscarPorCliente(clienteId);
    }

    @GetMapping("/seguimiento/{codigo}")
    public Despacho buscarPorCodigo(
            @PathVariable String codigo) {

        return service.buscarPorCodigo(codigo);
    }

    @PostMapping("/pedido/{pedidoId}")
    public Despacho crearDespacho(
            @PathVariable Long pedidoId) {

        return service.crearDespacho(pedidoId);
    }

    @PutMapping("/{id}/estado/{estado}")
    public Despacho actualizarEstado(
            @PathVariable Long id,
            @PathVariable String estado) {

        return service.actualizarEstado(
                id,
                estado);
    }

    @PutMapping("/{id}/entregar")
    public Despacho confirmarEntrega(
            @PathVariable Long id) {

        return service.confirmarEntrega(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        service.eliminar(id);
    }
}