package com.dreamysips.service_pedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dreamysips.service_pedidos.model.DetallePedido;
import com.dreamysips.service_pedidos.model.Pedido;
import com.dreamysips.service_pedidos.service.DetallePedidoService;
import com.dreamysips.service_pedidos.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con los pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Operation(summary = "Crear un pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public Pedido crear(@RequestBody Pedido pedido) {
        return pedidoService.guardar(pedido);
    }

    @Operation(summary = "Listar todos los pedidos")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.listarTodos();
    }

    @Operation(summary = "Buscar pedido por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(
            @Parameter(description = "ID del pedido")
            @PathVariable Long id) {

        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener pedido completo con información de cliente y dirección")
    @ApiResponse(responseCode = "200", description = "Pedido completo obtenido correctamente")
    @GetMapping("/{id}/completo")
    public Pedido obtenerCompleto(
            @Parameter(description = "ID del pedido")
            @PathVariable Long id) {

        return pedidoService.obtenerPedidoCompleto(id);
    }

    @Operation(summary = "Buscar pedidos por RUN del cliente")
    @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
    @GetMapping("/cliente/{run}")
    public List<Pedido> buscarPorCliente(
            @Parameter(description = "RUN del cliente")
            @PathVariable Long run) {

        return pedidoService.buscarPorRunCliente(run);
    }

    @Operation(summary = "Buscar pedidos por estado")
    @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
    @GetMapping("/estado/{estado}")
    public List<Pedido> buscarPorEstado(
            @Parameter(description = "Estado del pedido")
            @PathVariable String estado) {

        return pedidoService.buscarPorEstado(estado);
    }

    @Operation(summary = "Eliminar pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido eliminado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del pedido")
            @PathVariable Long id) {

        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar detalle a un pedido")
    @ApiResponse(responseCode = "200", description = "Detalle agregado correctamente")
    @PostMapping("/{idPedido}/detalles")
    public ResponseEntity<DetallePedido> agregarDetalle(
            @Parameter(description = "ID del pedido")
            @PathVariable Long idPedido,
            @RequestBody DetallePedido detalle) {

        return ResponseEntity.ok(
                detallePedidoService.guardarConPedido(idPedido, detalle));
    }

    @Operation(summary = "Vincular un detalle existente a un pedido")
    @ApiResponse(responseCode = "200", description = "Detalle vinculado correctamente")
    @PostMapping("/{idPedido}/detalles/{idDetalle}")
    public ResponseEntity<DetallePedido> vinculaConId(
            @Parameter(description = "ID del pedido")
            @PathVariable Long idPedido,

            @Parameter(description = "ID del detalle")
            @PathVariable Long idDetalle) {

        return ResponseEntity.ok(
                detallePedidoService.vincularConIdDetalle(idPedido, idDetalle));
    }
}