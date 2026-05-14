package com.dreamysips.service_pedidos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamysips.service_pedidos.client.ClienteClient;
import com.dreamysips.service_pedidos.dto.ClienteDto;
import com.dreamysips.service_pedidos.dto.DireccionEnvioDto;
import com.dreamysips.service_pedidos.model.Pedido;
import com.dreamysips.service_pedidos.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService 
{

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteClient clienteClient;

    public List <Pedido> listarTodos() 
    {
        return pedidoRepository.findAll();
    }

    public Optional <Pedido> buscarPorId (Long idPedido)
    {
        return pedidoRepository.findById(idPedido);
    }

    public List <Pedido> buscarPorRunCliente (Long runCliente) 
    {
        return pedidoRepository.findPedidosByRunCliente (runCliente);
    }

    @Transactional
    public Pedido guardar (Pedido pedido)
    {
        return pedidoRepository.save(pedido);
    }

    
    public void eliminar (Long idPedido)
    {
        pedidoRepository.deleteById(idPedido);
    }

    public List <Pedido> buscarPorEstado (String estado)
    {
        return pedidoRepository.findByEstado(estado);
    }

    public List<Pedido> buscarPorFecha(LocalDateTime fechaPedido) 
    {
        return pedidoRepository.findByFechaPedido(fechaPedido);
    }

    //Metodos feign 

    // Obtener datos del cliente de un pedido
    public ClienteDto obtenerClienteDelPedido(Long idPedido) 
    {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return clienteClient.obtenerCliente(pedido.getRunCliente());
    }

    // Obtener dirección de envío de un pedido
    public DireccionEnvioDto obtenerDireccionDelPedido(Long idPedido) 
    {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return clienteClient.obtenerDireccion(pedido.getIdDireccionEnvio());
    }



}