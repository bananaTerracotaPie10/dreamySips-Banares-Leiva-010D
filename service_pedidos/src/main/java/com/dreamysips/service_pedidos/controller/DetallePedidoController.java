package com.dreamysips.service_pedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dreamysips.service_pedidos.model.DetallePedido;
import com.dreamysips.service_pedidos.service.DetallePedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/detalles")
@Tag(name = "Detalle del Pedido", description = "Operaciones relacionadas con los detalles de pedidos")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Operation(summary = "Crear detalle de pedido")
    @ApiResponse(responseCode = "200", description = "Detalle creado correctamente")
    @PostMapping("/{idPedido}")
    public DetallePedido crear(
            @Parameter(description = "ID del pedido")
            @PathVariable Long idPedido,

            @RequestBody DetallePedido detalle) {

        return detallePedidoService.guardar(idPedido, detalle);
    }

    @Operation(summary = "Listar todos los detalles")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public List<DetallePedido> listar() {
        return detallePedidoService.listarTodos();
    }

    @Operation(summary = "Buscar detalle por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle encontrado"),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> buscarPorId(
            @Parameter(description = "ID del detalle")
            @PathVariable Long id) {

        return detallePedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener detalle completo con información del producto")
    @ApiResponse(responseCode = "200", description = "Detalle completo obtenido")
    @GetMapping("/{id}/completo")
    public DetallePedido obtenerCompleto(
            @Parameter(description = "ID del detalle")
            @PathVariable Long id) {

        return detallePedidoService.obtenerDetalleCompleto(id);
    }

    @Operation(summary = "Listar detalles de un pedido con información de productos")
    @ApiResponse(responseCode = "200", description = "Detalles encontrados")
    @GetMapping("/pedido/{idPedido}")
    public List<DetallePedido> listarPorPedidoCompleto(
            @Parameter(description = "ID del pedido")
            @PathVariable Long idPedido) {

        return detallePedidoService.listarPorPedidoCompleto(idPedido);
    }

    @Operation(summary = "Eliminar detalle")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Detalle eliminado"),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del detalle")
            @PathVariable Long id) {

        detallePedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}