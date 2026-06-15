package com.example.service_notificacion.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.service_notificacion.dto.Clientedto;
import com.example.service_notificacion.dto.Notificaciondto;
import com.example.service_notificacion.dto.Pedidodto;
import com.example.service_notificacion.model.Notificacion;
import com.example.service_notificacion.repository.NotificacionRepository;


@Service
public class NotificacionService {

    private final NotificacionRepository repository;
    private final WebClient webClient;
    

    public NotificacionService(NotificacionRepository repository, WebClient webClient) {

        this.repository = repository;
        this.webClient = webClient;
    }

    public List<Notificacion> listar() {
        return repository.findAll();
    }

    public Notificacion buscar(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Notificacion> buscarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public Notificacion enviar(Notificaciondto dto) {

        Clientedto cliente = webClient.get()
                .uri("http://localhost:8081/clientes/" + dto.getClienteId())
                .retrieve()
                .bodyToMono(Clientedto.class)
                .block();

        Pedidodto pedido = webClient.get()
                .uri("http://localhost:8084/pedidos/" + dto.getPedidoId())
                .retrieve()
                .bodyToMono(Pedidodto.class)
                .block();

        Notificacion notificacion = Notificacion.builder()
                .clienteId(cliente.getId())
                .pedidoId(pedido.getId())
                .tipo(dto.getTipo())
                .asunto(dto.getAsunto())
                .mensaje(dto.getMensaje())
                .fechaEnvio(LocalDateTime.now())
                .estado("ENVIADO")
                .leido(false)
                .build();

        return repository.save(notificacion);
    }

    public Notificacion marcarLeido(Long id) {

        Notificacion n = repository.findById(id).orElseThrow();

        n.setLeido(true);
        n.setEstado("LEIDO");

        return repository.save(n);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}