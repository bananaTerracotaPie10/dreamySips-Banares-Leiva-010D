package com.dreamysips.service_pedidos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_pedidos.DTO.ClienteDto;
import com.dreamysips.service_pedidos.DTO.DireccionEnvioDto;
import com.dreamysips.service_pedidos.model.Pedido;
import com.dreamysips.service_pedidos.repository.PedidoRepository;

@Service
public class PedidoService 
{

    private final DetallePedidoService detallePedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;
 
    @Autowired
    private WebClient.Builder webClientBuilder;

    // ruta configurada en application.properties
    
    @Value("${services.cliente.url}")
    private String clienteUrl;

    @Value("${services.catalogo.url}")
    private String catalogoUrl;

    PedidoService(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    //CRUD

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
 
    public Optional<Pedido> buscarPorId(Long idPedido) 
    {
        return pedidoRepository.findById(idPedido);
    }
 
    public List<Pedido> buscarPorRunCliente(Long runCliente) 
    {
        return pedidoRepository.findByRunCliente(runCliente);
    }
 
    public List<Pedido> buscarPorEstado(String estado) 
    {
        return pedidoRepository.findByEstado(estado);
    }
 
    public Pedido guardar(Pedido pedido) 
    {
        return pedidoRepository.save(pedido);
    }
 
    public void eliminar(Long idPedido) 
    {
        pedidoRepository.deleteById(idPedido);
    }

    // Metodos para obtener datos externos 

    // entrega los datos del pedido + datosCliente + datosDireccion

    public Pedido obtenerPedidoCompleto(Long idPedido) 
    {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + idPedido));
        return enriquecerConDatosExternos(pedido);
    }

    //obtiene datos del cliente + datos de la direccion 

    private Pedido enriquecerConDatosExternos(Pedido pedido) 
    {
        pedido.setDatosCliente(obtenerCliente(pedido.getRunCliente()));
        pedido.setDatosDireccion(obtenerDireccion(pedido.getIdDireccionEnvio()));
        pedido.setDetalles(detallePedidoService.listarPorPedidoCompleto(pedido.getIdPedido()));

        return pedido;
    }

    // Llama al service-cliente para traer al cliente

    private ClienteDto obtenerCliente(Long runCliente) 
    {
        if (runCliente == null) return null;
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(clienteUrl + "/api/v1/clientes/" + runCliente)
                    .retrieve()
                    .bodyToMono(ClienteDto.class)
                    .block();
        } catch (Exception e) {
            System.err.println("No se pudo obtener cliente " + runCliente + ": " + e.getMessage());
            return null;
        }
    }

    
     // Llama al service-cliente para traer la direccion de envio
    
    private DireccionEnvioDto obtenerDireccion(Long idDireccion) 
    {
        if (idDireccion == null) return null;
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(clienteUrl + "/api/v1/direcciones/" + idDireccion)
                    .retrieve()
                    .bodyToMono(DireccionEnvioDto.class)
                    .block();
        } catch (Exception e) {
            System.err.println("No se pudo obtener dirección " + idDireccion + ": " + e.getMessage());
            return null;
        }
    }

}
