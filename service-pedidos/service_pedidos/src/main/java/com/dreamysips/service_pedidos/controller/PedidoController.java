package com.dreamysips.service_pedidos.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamysips.service_pedidos.dto.ClienteDto;
import com.dreamysips.service_pedidos.dto.DireccionEnvioDto;
import com.dreamysips.service_pedidos.model.DetallePedido;
import com.dreamysips.service_pedidos.model.Pedido;
import com.dreamysips.service_pedidos.service.DetallePedidoService;
import com.dreamysips.service_pedidos.service.PedidoService;



@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController 
{

    @Autowired
    private PedidoService pedidoService;


    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    public List <Pedido> listarPedidos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity <Pedido> obtenerPorId (@PathVariable long idPedido)
    {
        return pedidoService.buscarPorId(idPedido)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{run}")
    public ResponseEntity<List<Pedido>> buscarPorRunCliente(@PathVariable long run) 
    {
        List<Pedido> pedidos = pedidoService.buscarPorRunCliente(run);

        if (pedidos.isEmpty()) 
        {
            return ResponseEntity.notFound().build();
        }

    return ResponseEntity.ok(pedidos);
    } 
    
    @GetMapping("/estado/{estado}")
    public List<Pedido> obtenerPorEstado(@PathVariable String estado) 
    {
        return pedidoService.buscarPorEstado(estado);
    }

    @GetMapping("/fecha/{fecha}")
    public List<Pedido> obtenerPorFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) 
    {
        return pedidoService.buscarPorFecha(fecha);
    }

    @PostMapping
    public ResponseEntity <Pedido> crear (@RequestBody Pedido pedido)
    {
        return ResponseEntity.ok (pedidoService.guardar(pedido));
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> eliminar (@PathVariable long idPedido)
    {
        pedidoService.eliminar(idPedido);
        return ResponseEntity.noContent().build();
    }

    // Ruta anidada para detalles
    @PostMapping("/{idPedido}/detalles")
    public ResponseEntity<DetallePedido> agregarDetalle(
            @PathVariable Long idPedido,
            @RequestBody DetallePedido detalle) {
        return ResponseEntity.ok(detallePedidoService.guardarConPedido(idPedido, detalle));
    }

    //feign

    @GetMapping("/{idPedido}/cliente")
    public ResponseEntity<ClienteDto> obtenerClienteDelPedido(@PathVariable Long idPedido) 
    {
        return ResponseEntity.ok(pedidoService.obtenerClienteDelPedido(idPedido));
    }

    @GetMapping("/{idPedido}/direccion")
    public ResponseEntity<DireccionEnvioDto> obtenerDireccionDelPedido(@PathVariable Long idPedido) 
    {
        return ResponseEntity.ok(pedidoService.obtenerDireccionDelPedido(idPedido));
    }


}
