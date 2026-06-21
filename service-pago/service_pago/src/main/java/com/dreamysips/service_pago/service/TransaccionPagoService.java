package com.dreamysips.service_pago.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_pago.model.Pago;
import com.dreamysips.service_pago.model.TransaccionPago;
import com.dreamysips.service_pago.repository.PagoRepository;
import com.dreamysips.service_pago.repository.TransaccionPagoRepository;

import jakarta.transaction.Transactional;

@Service
public class TransaccionPagoService 
{

    @Autowired
    private TransaccionPagoRepository transaccionPagoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<TransaccionPago> listarTodos() {
        return transaccionPagoRepository.findAll();
    }

    public Optional <TransaccionPago> buscarPorId (Long idTransaccion)
    {
        return transaccionPagoRepository.findById(idTransaccion);
    }

    public List<TransaccionPago> buscarPorTipoTransaccion (String tipoTransaccion)
    {
        return transaccionPagoRepository.findByTipoTransaccion(tipoTransaccion);
    }

    public List<TransaccionPago> buscarPorEstadoTransaccion (String estadoTransaccion)
    {
        return transaccionPagoRepository.findByEstadoTransaccion(estadoTransaccion);
    }

    public List<TransaccionPago> buscarPorFechaTransaccion (LocalDateTime fechaTransaccion)
    {
        return transaccionPagoRepository.findByFechaTransaccion(fechaTransaccion);
    }

    @Transactional
    public TransaccionPago guardar(Long idPago, TransaccionPago transaccion)
    {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + idPago));
        transaccion.setPagos(pago);
        return transaccionPagoRepository.save(transaccion);
    }

    public void eliminar (Long idTransaccion)
    {
        transaccionPagoRepository.deleteById(idTransaccion);
    }

    // guarda detalles a un pedido existente

    @Transactional
    public TransaccionPago guardarConPago (Long idPago, TransaccionPago transaccion)
    {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id:" + idPago));
        transaccion.setPagos(pago);
        return transaccionPagoRepository.save(transaccion);
    }

    //vincula con la id

    public TransaccionPago vincularConIdTransaccion (Long idPago, Long idTransaccion)
    {
        Pago pago = pagoRepository.findById(idPago)
              .orElseThrow(() -> new RuntimeException("Pago no encontrado con id:" + idPago));
        
        TransaccionPago transaccion = transaccionPagoRepository.findById(idTransaccion)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada con id:" + idTransaccion));

        transaccion.setPagos(pago);
        return transaccionPagoRepository.save(transaccion);
    }



}
