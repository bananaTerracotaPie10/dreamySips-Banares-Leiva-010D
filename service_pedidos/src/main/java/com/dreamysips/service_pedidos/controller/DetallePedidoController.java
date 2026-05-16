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
import com.dreamysips.service_pedidos.service.DetallePedidoService;

@RestController
@RequestMapping("/api/v1/detalles")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    //  agrega (crea) el detalle al pedido

    @PostMapping("/{idPedido}")
    public DetallePedido crear(@PathVariable Long idPedido, @RequestBody DetallePedido detalle) 
    {
        return detallePedidoService.guardar(idPedido, detalle);
    }

    // retorna una lista con todos los detalles

    @GetMapping
    public List<DetallePedido> listar() {
        return detallePedidoService.listarTodos();
    }

    // busca el detalle x id

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> buscarPorId(@PathVariable Long id) 
    {
        return detallePedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // retorna el detalle con datosProducto enriquecido (con enriquecimiento quiero decir que trae datos externos)

    @GetMapping("/{id}/completo")
    public DetallePedido obtenerCompleto(@PathVariable Long id) 
    {
        return detallePedidoService.obtenerDetalleCompleto(id);
    }

    // retorna una lista con todos los detalles de un pedido con productos
    
    @GetMapping("/pedido/{idPedido}")
    public List<DetallePedido> listarPorPedidoCompleto(@PathVariable Long idPedido) 
    {
        return detallePedidoService.listarPorPedidoCompleto(idPedido);
    }

    // eliminar

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) 
    {
        detallePedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
