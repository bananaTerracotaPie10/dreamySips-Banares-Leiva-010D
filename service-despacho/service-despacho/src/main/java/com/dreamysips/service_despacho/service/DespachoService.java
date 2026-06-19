package com.dreamysips.service_despacho.service;

import com.dreamysips.service_despacho.dto.ClienteDTO;
import com.dreamysips.service_despacho.dto.NotificacionDTO;
import com.dreamysips.service_despacho.dto.PedidoDTO;
import com.dreamysips.service_despacho.model.Despacho;
import com.dreamysips.service_despacho.repository.DespachoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DespachoService {

    private final DespachoRepository repository;
    private final WebClient webClient;

    public DespachoService(
            DespachoRepository repository,
            WebClient webClient) {

        this.repository = repository;
        this.webClient = webClient;
    }

    public List<Despacho> listar() {
        return repository.findAll();
    }

    public Despacho buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Despacho no encontrado"));
    }


    public List<Despacho> buscarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public Despacho buscarPorCodigo(String codigo) {
        return repository.findByCodigoSeguimiento(codigo)
                .orElseThrow(() ->
                        new RuntimeException("Código no encontrado"));
    }

    public Despacho crearDespacho(Long pedidoId) {

        PedidoDTO pedido = webClient.get()
                .uri("http://localhost:8085/pedidos/" + pedidoId)
                .retrieve()
                .bodyToMono(PedidoDTO.class)
                .block();

        ClienteDTO cliente = webClient.get()
                .uri("http://localhost:8082/clientes/" +
                        pedido.getClienteId())
                .retrieve()
                .bodyToMono(ClienteDTO.class)
                .block();

        Despacho despacho = new Despacho();

        despacho.setPedidoId(pedidoId);
        despacho.setClienteId(cliente.getId());

        despacho.setEstado("PREPARANDO");

        despacho.setFechaDespacho(LocalDate.now());

        despacho.setFechaEstimada(
                LocalDate.now().plusDays(5));

        despacho.setCodigoSeguimiento(
                UUID.randomUUID().toString());

        return repository.save(despacho);
    }


    public Despacho actualizarEstado(Long id, String estado) {

        Despacho despacho = buscar(id);

        despacho.setEstado(estado);

        if ("EN_TRANSITO".equals(estado)) {

            NotificacionDTO notificacion = new NotificacionDTO();

            notificacion.setClienteId(despacho.getClienteId());

            notificacion.setTipo("EMAIL");

            notificacion.setAsunto("Pedido en tránsito");

            notificacion.setMensaje("Tu pedido ya va en camino");

            webClient.post()
                    .uri("http://localhost:8086/notificaciones/enviar")
                    .bodyValue(notificacion)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }

        return repository.save(despacho);
    }

    public Despacho confirmarEntrega(Long id) {

        Despacho despacho = buscar(id);

        despacho.setEstado("ENTREGADO");

        despacho.setFechaEntrega(
                LocalDate.now());

        return repository.save(despacho);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
