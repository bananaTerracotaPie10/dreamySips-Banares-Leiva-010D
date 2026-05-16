package com.dreamysips.service_pedidos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_pedidos.DTO.ProductoDto;
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
    private WebClient.Builder webClientBuilder;

    // ruta configurada en application.properties
    @Value("${services.catalogo.url}")
    private String catalogoUrl;

    
    //  CRUD 
    

    public List<DetallePedido> listarTodos() {
        return detallePedidoRepository.findAll();
    }

    public Optional<DetallePedido> buscarPorId(Long idDetalle) {
        return detallePedidoRepository.findById(idDetalle);
    }

    public List<DetallePedido> listarPorPedido(Long idPedido) {
        return detallePedidoRepository.findByPedido_IdPedido(idPedido);
    }

    @Transactional
    public DetallePedido guardar(Long idPedido, DetallePedido detalle) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + idPedido));
        detalle.setPedido(pedido);
        return detallePedidoRepository.save(detalle);
    }

    public void eliminar(Long idDetalle) {
        detallePedidoRepository.deleteById(idDetalle);
    }


    // detalle completo 

    public DetallePedido obtenerDetalleCompleto(Long idDetalle) 
    {
        DetallePedido detalle = detallePedidoRepository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado: " + idDetalle));
        return enriquecerConProducto(detalle);
    }

    
     // Retorna todos los detalles de un pedido, cada uno con su producto enriquecido.
     
    public List<DetallePedido> listarPorPedidoCompleto(Long idPedido) 
    {
        List<DetallePedido> detalles = detallePedidoRepository.findByPedido_IdPedido(idPedido);
        detalles.forEach(this::enriquecerConProducto);
        return detalles;
    }

    
    // + Métodos de enriquecimiento $$$
    

    private DetallePedido enriquecerConProducto(DetallePedido detalle) 
    {
        detalle.setDatosProducto(obtenerProducto(detalle.getIdProducto()));
        return detalle;
    }

    
    // Llama al service-catalogo para traer datos del Producto. De no encontrar nada, retorna null.
    
    private ProductoDto obtenerProducto(Long idProducto) 
    {
        if (idProducto == null) return null;
        try 
        {
            return webClientBuilder.build()
                    .get()
                    .uri(catalogoUrl + "/api/v1/producto/" + idProducto)
                    .retrieve()
                    .bodyToMono(ProductoDto.class)
                    .block();
        } catch (Exception e) {
            System.err.println("No se pudo obtener producto " + idProducto + ": " + e.getMessage());
            return null;
        }
    }

    // metodos para guardar detalles a un pedido existente

    @Transactional
    public DetallePedido guardarConPedido(Long idPedido, DetallePedido detalle)
    {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id:" + idPedido));
        detalle.setPedido(pedido);
        return detallePedidoRepository.save(detalle);
    }

    //este vincula con la id

    public DetallePedido vincularConIdDetalle (Long idPedido, Long idDetalle)
    {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));

        DetallePedido detalle = detallePedidoRepository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado con id: " + idDetalle));

        detalle.setPedido(pedido);
        return detallePedidoRepository.save(detalle);
    }

}
