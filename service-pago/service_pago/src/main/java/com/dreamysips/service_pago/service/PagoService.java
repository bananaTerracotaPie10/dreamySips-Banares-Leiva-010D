package com.dreamysips.service_pago.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_pago.dto.PedidoDto;
import com.dreamysips.service_pago.model.Pago;
import com.dreamysips.service_pago.repository.PagoRepository;


@Service
public class PagoService 
{

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value ("${services.pedidos.url}")
    private String pedidoUrl;

    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    public Optional <Pago> buscarPorId(Long idPago) 
    {
        return pagoRepository.findById(idPago);
    }

    public List<Pago> buscarPorIdPedido(Long idPedido) 
    {
        return pagoRepository.findByIdPedido(idPedido);
    }

    public List<Pago> buscarPorEstado(String estado) 
    {
        return pagoRepository.findByEstado(estado);
    }

    public Pago guardar(Pago pago) 
    {
        return pagoRepository.save(pago);
    }

    public Void eliminar(Long idPago) 
    {
        pagoRepository.deleteById(idPago);
        return null;
    }

    public Pago obtenerPagoConDatosExternos (Long idPago)
    {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + idPago));
        return enriquererConDatosExternos(pago);
    }

    private Pago enriquererConDatosExternos (Pago pago)
    {
        if (pago == null) return null;
        pago.setDatosPedido(obtenerIdPedido(pago.getIdPedido()));
        return pago;
    }

    private PedidoDto obtenerIdPedido (Long idPedido)
    {
        if (idPedido == null) return null;
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(pedidoUrl + "/api/v1/pedidos" + idPedido)
                    .retrieve()
                    .bodyToMono(PedidoDto.class)
                    .block();
        } catch (Exception e) {
            System.err.println("No se pudo obtener pedido " + idPedido + ": " + e.getMessage());
            return null;
        }
            
    }

    

}
