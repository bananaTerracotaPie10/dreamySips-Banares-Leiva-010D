package com.dreamysips.service_listadeseos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_listadeseos.dto.*;
import com.dreamysips.service_listadeseos.model.ListaDeseos;
import com.dreamysips.service_listadeseos.repository.ListaDeseosRepository;

@Service
public class ListaDeseosService {

    private final ListaDeseosRepository repository;
    private final WebClient webClient;

    public ListaDeseosService(
            ListaDeseosRepository repository,
            WebClient webClient) {

        this.repository = repository;
        this.webClient = webClient;
    }

    public List<ListaDeseos> listar() {
        return repository.findAll();
    }

    public ListaDeseos buscar(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<ListaDeseos> buscarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public ListaDeseos agregar(Long clienteId, Long productoId,
            Boolean notificarDescuento) {

        Clientedto cliente = webClient.get()
                .uri("http://localhost:8081/clientes/" + clienteId)
                .retrieve()
                .bodyToMono(Clientedto.class)
                .block();

        Productodto producto = webClient.get()
                .uri("http://localhost:8082/productos/" + productoId)
                .retrieve()
                .bodyToMono(Productodto.class)
                .block();

        ListaDeseos item = ListaDeseos.builder()
                .clienteId(cliente.getId())
                .productoId(producto.getId())
                .precioRegistrado(producto.getPrecio())
                .notificarDescuento(notificarDescuento)
                .fechaAgregado(LocalDateTime.now())
                .build();

        return repository.save(item);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public void verificarDescuento(Long productoId) {

        Productodto productoActual = webClient.get()
                .uri("http://localhost:8082/productos/" + productoId)
                .retrieve()
                .bodyToMono(Productodto.class)
                .block();

        List<ListaDeseos> interesados =
                repository.findByProductoId(productoId);

        for(ListaDeseos item : interesados) {

            if(item.getNotificarDescuento()
                    && productoActual.getPrecio()
                    < item.getPrecioRegistrado()) {

                Notificaciondto noti = new Notificaciondto();

                noti.setClienteId(item.getClienteId());
                noti.setTipo("EMAIL");
                noti.setAsunto("Producto con descuento");
                noti.setMensaje(
                        "El producto "
                        + productoActual.getNombre()
                        + " ha bajado de precio.");

                webClient.post()
                        .uri("http://localhost:8085/notificaciones/enviar")
                        .bodyValue(noti)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();

                item.setPrecioRegistrado(
                        productoActual.getPrecio());

                repository.save(item);
            }
        }
    }

}
