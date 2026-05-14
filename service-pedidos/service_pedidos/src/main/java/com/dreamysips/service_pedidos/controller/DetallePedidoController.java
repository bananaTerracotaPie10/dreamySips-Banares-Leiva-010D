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

import com.dreamysips.service_pedidos.dto.ProductoDto;
import com.dreamysips.service_pedidos.model.DetallePedido;
import com.dreamysips.service_pedidos.service.DetallePedidoService;

@RestController
@RequestMapping("/api/v1/detalles")
public class DetallePedidoController 
{

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    public List <DetallePedido> listarDetalles() 
    {
        return detallePedidoService.getAllDetalles();
    }

    @GetMapping("/{idDetalle}")
    public ResponseEntity <DetallePedido> obtenerPorId (@PathVariable long idDetalle)
    {
        return detallePedidoService.buscarPorId(idDetalle)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity <DetallePedido> crear (@RequestBody DetallePedido detallePedido)
    {
        return ResponseEntity.ok (detallePedidoService.guardar(detallePedido));
    }

    @DeleteMapping("/{idDetalle}")
    public ResponseEntity<Void> eliminar (@PathVariable long idDetalle)
    {
        detallePedidoService.eliminar(idDetalle);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detalles/{idDetalle}/producto")
    public ResponseEntity<ProductoDto> obtenerProductoDelDetalle(@PathVariable Long idDetalle) 
    {
        return ResponseEntity.ok(detallePedidoService.obtenerProductoDelDetalle(idDetalle));
    }

}
