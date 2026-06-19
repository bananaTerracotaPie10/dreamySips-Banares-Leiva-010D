package com.dreamysips.service_listadeseos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dreamysips.service_listadeseos.model.ListaDeseos;
import com.dreamysips.service_listadeseos.service.ListaDeseosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/listadeseos")
@Tag(name = "Lista de Deseos", description = "Operaciones relacionadas con la lista de deseos")
public class ListaDeseosController {

    private final ListaDeseosService service;

    public ListaDeseosController(ListaDeseosService service) {
        this.service = service;
    }

    @Operation(summary = "Listar elementos de la lista de deseos")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<ListaDeseos>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar elemento de la lista de deseos por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Elemento encontrado"),
        @ApiResponse(responseCode = "404", description = "Elemento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ListaDeseos> buscar(
            @Parameter(description = "ID del elemento de la lista de deseos")
            @PathVariable Long id) {

        return ResponseEntity.ok(service.buscar(id));
    }

    @Operation(summary = "Buscar lista de deseos por cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista encontrada"),
        @ApiResponse(responseCode = "404", description = "Cliente sin elementos en la lista")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ListaDeseos>> buscarCliente(
            @Parameter(description = "ID del cliente")
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                service.buscarPorCliente(clienteId));
    }

    @Operation(summary = "Agregar producto a la lista de deseos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ListaDeseos> agregar(

            @Parameter(description = "ID del cliente")
            @RequestParam Long clienteId,

            @Parameter(description = "ID del producto")
            @RequestParam Long productoId,

            @Parameter(description = "Indica si se debe notificar cuando exista un descuento")
            @RequestParam Boolean notificarDescuento) {

        return ResponseEntity.ok(
                service.agregar(
                        clienteId,
                        productoId,
                        notificarDescuento));
    }

    @Operation(summary = "Verificar descuentos de un producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificación completada"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PostMapping("/verificar-descuento/{productoId}")
    public ResponseEntity<String> verificarDescuento(

            @Parameter(description = "ID del producto")
            @PathVariable Long productoId) {

        service.verificarDescuento(productoId);

        return ResponseEntity.ok(
                "Verificación completada");
    }

    @Operation(summary = "Eliminar elemento de la lista de deseos")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Elemento eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Elemento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(

            @Parameter(description = "ID del elemento de la lista de deseos")
            @PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}