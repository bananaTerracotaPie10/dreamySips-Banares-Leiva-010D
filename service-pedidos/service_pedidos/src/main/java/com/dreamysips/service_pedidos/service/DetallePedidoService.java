package com.dreamysips.service_pedidos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamysips.service_pedidos.client.ProductoClient;
import com.dreamysips.service_pedidos.dto.ProductoDto;
import com.dreamysips.service_pedidos.model.DetallePedido;
import com.dreamysips.service_pedidos.model.Pedido;
import com.dreamysips.service_pedidos.repository.DetallePedidoRepository;
import com.dreamysips.service_pedidos.repository.PedidoRepository;

import jakarta.transaction.Transactional;


@Service
public class DetallePedidoService 
{

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoClient productoClient;

    public List <DetallePedido> getAllDetalles() 
    {
        return detallePedidoRepository.findAll();
    }

    public Optional <DetallePedido> buscarPorId (Long idDetalle)
    {
        return detallePedidoRepository.findById(idDetalle);
    }

    @Transactional
    public DetallePedido guardar (DetallePedido detallePedido)
    {
        return detallePedidoRepository.save(detallePedido);
    }

    public void eliminar (Long idDetalle)
    {
        detallePedidoRepository.deleteById(idDetalle);
    }

    @Transactional
    public DetallePedido guardarConPedido(Long idPedido, DetallePedido detalle) 
    {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        detalle.setPedido(pedido);
        return detallePedidoRepository.save(detalle);
    }

    //Metodo feign
    //obtiene datos del producto de un detalle de pedido

    public ProductoDto obtenerProductoDelDetalle(Long idDetalle) 
    {
        DetallePedido detalle = detallePedidoRepository.findById(idDetalle)
            .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
            return productoClient.obtenerProducto(detalle.getIdProducto());
    }

}
