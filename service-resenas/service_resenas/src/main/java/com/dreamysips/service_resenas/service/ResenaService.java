package com.dreamysips.service_resenas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_resenas.DTO.ClienteDto;
import com.dreamysips.service_resenas.DTO.PedidoDto;
import com.dreamysips.service_resenas.DTO.ProductoDto;
import com.dreamysips.service_resenas.model.Resena;
import com.dreamysips.service_resenas.repository.ResenaRepository;

@Service
public class ResenaService 
{

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${services.clientes.url}")
    private String clientesurl;

    @Value("${services.catalogo.url}")
    private String catalogourl;

    @Value("${services.pedidos.url}")
    private String pedidosurl;
    

    // CRUD

    public List <Resena> listarResenas ()
    {
        return resenaRepository.findAll();
    }

    public Optional <Resena> buscarPorId(Long idResena)
    {
        return resenaRepository.findById(idResena);
    }

    public List<Resena> buscarPorRunCliente (Long runCliente)
    {
        return resenaRepository.findByRunCliente(runCliente);
    }

    public List<Resena> buscarPorIdProducto (Long idProducto)
    {
        return resenaRepository.findByIdProducto(idProducto);
    }

    public List <Resena> buscarPorPuntuacion (int puntuacion)
    {
        return resenaRepository.findByPuntuacion(puntuacion);
    }
    //crear
    public Resena crearResena (Resena resena)
    {
        
        PedidoDto pedido = obtenerPedido (resena.getIdPedido());

        if (pedido == null)
        {
            throw new RuntimeException("No se encontro el pedido");
        }

        if (!pedido.getRunCliente().equals(resena.getRunCliente()))
        {
            throw new RuntimeException("El pedido no pertenece al cliente");
        }

        resena.setFecha(LocalDateTime.now());
        Resena guardar =  resenaRepository.save(resena);

        return obtenerConDatosExternos(guardar);

    }

    //eliminar

    public void eliminarResena (Long idResena)
    {
        resenaRepository.deleteById(idResena);
    }

    //enriquecimiento $$$

    public Resena obtenerResenaCompleta(Long idResena) 
    {
        Resena resena = resenaRepository.findById(idResena)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada: " + idResena));
        return obtenerConDatosExternos(resena);
    }

    private Resena obtenerConDatosExternos(Resena resena) 
    {
        resena.setDatosCliente(obtenerCliente(resena.getRunCliente()));
        resena.setDatosPedido(obtenerPedido(resena.getIdPedido()));
        resena.setDatosProducto(obtenerProductoDto(resena.getIdProducto()));
        return resena;
    }

    private ClienteDto obtenerCliente(Long runCliente) 
    {
        if (runCliente == null) return null;
        try 
        {
            return webClientBuilder.build()
                    .get()
                    .uri(clientesurl + "/api/v1/clientes/" + runCliente)
                    .retrieve()
                    .bodyToMono(ClienteDto.class)
                    .block();
        } catch (Exception e) {
            System.err.println("No se pudo obtener cliente " + runCliente + ": " + e.getMessage());
            return null;
        }
    }

    private PedidoDto obtenerPedido(Long idPedido) 
    {
        if (idPedido == null) return null;
        try 
        {
            return webClientBuilder.build()
                    .get()
                    .uri(pedidosurl + "/api/v1/pedidos/" + idPedido)
                    .retrieve()
                    .bodyToMono(PedidoDto.class)
                    .block();
        } catch (Exception e) 
        {
            System.err.println("No se pudo obtener pedido " + idPedido + ": " + e.getMessage());
            return null;
        }
    }

    private ProductoDto obtenerProductoDto(Long idProducto) 
    {
        if (idProducto == null) return null;
        try 
        {
            return webClientBuilder.build()
                    .get()
                    .uri(catalogourl + "/api/v1/producto/" + idProducto)
                    .retrieve()
                    .bodyToMono(ProductoDto.class)
                    .block();
        } catch (Exception e) 
        {
            System.err.println("No se pudo obtener producto " + idProducto + ": " + e.getMessage());
            return null;
        }
    }


}
