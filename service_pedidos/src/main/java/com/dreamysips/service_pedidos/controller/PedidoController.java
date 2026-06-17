package com.dreamysips.service_pedidos.controller;

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

import com.dreamysips.service_pedidos.model.DetallePedido;
import com.dreamysips.service_pedidos.model.Pedido;
import com.dreamysips.service_pedidos.service.DetallePedidoService;
import com.dreamysips.service_pedidos.service.PedidoService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Operaciones de Pedidos")
public class PedidoController 
{

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    // crea un pedido (sin datos externos)

    @PostMapping
    public Pedido crear(@RequestBody Pedido pedido) 
    {
        return pedidoService.guardar(pedido);
    }

    //  lista de todos los pedidos (sin externalidades)


    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.listarTodos();
    }

    //  entrega un pedido simple (sin externalidades) x id

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) 
    {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // retorna pedido con datosCliente y datosDireccion
    
    @GetMapping("/{id}/completo")
    public Pedido obtenerCompleto(@PathVariable Long id) {
        return pedidoService.obtenerPedidoCompleto(id);
    }

    // obtiene pedidos x cliente

    @GetMapping("/cliente/{run}")
    public List<Pedido> buscarPorCliente(@PathVariable Long run) {
        return pedidoService.buscarPorRunCliente(run);
    }

    // obtener pedidos x estado

    @GetMapping("/estado/{estado}")
    public List<Pedido> buscarPorEstado(@PathVariable String estado) {
        return pedidoService.buscarPorEstado(estado);
    }

    // borrar pedido 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //detalles

    //agrega detalle a pedido existente

    @PostMapping("/{idPedido}/detalles")
    public ResponseEntity<DetallePedido> agregarDetalle(
        @PathVariable Long idPedido,
        @RequestBody DetallePedido detalle)
    {
    return ResponseEntity.ok(detallePedidoService.guardarConPedido(idPedido, detalle));
    }

    //agrega detalla con id a pedido
    
    @PostMapping("/{idPedido}/detalles/{idDetalle}")
    public ResponseEntity<DetallePedido> vinculaConId(
        @PathVariable Long idPedido,
        @PathVariable Long idDetalle)
    {
        return ResponseEntity.ok(detallePedidoService.vincularConIdDetalle(idPedido, idDetalle));
    }
}
