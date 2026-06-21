package com.example.service_notificacion.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.service_notificacion.dto.Clientedto;
import com.example.service_notificacion.dto.Notificaciondto;
import com.example.service_notificacion.dto.Pedidodto;
import com.example.service_notificacion.model.Notificacion;
import com.example.service_notificacion.repository.NotificacionRepository;

import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Usamos Mockito para simular objetos
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository; // Simulamos el repositorio

    // El WebClient real no se simula con @Mock + @InjectMocks porque el Service
    // recibe un WebClient ya construido (no un WebClient.Builder). Lo simulamos
    // manualmente con Mockito.mock() y lo inyectamos por constructor.
    private WebClient webClient;

    private NotificacionService notificacionService;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        notificacionService = new NotificacionService(repository, webClient);
    }

    @Test
    @DisplayName("Deberia listar todas las notificaciones correctamente")
    void listarTest() {
        // 1. Preparacion (escenario)
        Notificacion n1 = Notificacion.builder().id(1L).clienteId(1L).pedidoId(1L)
                .tipo("EMAIL").estado("ENVIADO").leido(false).build();
        Notificacion n2 = Notificacion.builder().id(2L).clienteId(2L).pedidoId(2L)
                .tipo("SMS").estado("LEIDO").leido(true).build();

        when(repository.findAll()).thenReturn(Arrays.asList(n1, n2));

        // 2. Ejecucion (When)
        List<Notificacion> resultado = notificacionService.listar();

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificamos que el servicio no devolvio un nulo
        assertEquals(2, resultado.size()); // Comprobamos la cantidad esperada
        verify(repository, times(1)).findAll(); // Verificamos que el repositorio fue llamado exactamente 1 vez
    }

    @Test
    @DisplayName("Deberia buscar una notificacion por id correctamente")
    void buscarTest() {
        // 1. Preparacion (escenario)
        Long id = 1L;
        Notificacion notificacionMock = Notificacion.builder().id(id).clienteId(1L).pedidoId(1L)
                .tipo("EMAIL").estado("ENVIADO").leido(false).build();

        when(repository.findById(id)).thenReturn(Optional.of(notificacionMock));

        // 2. Ejecucion (When)
        Notificacion resultado = notificacionService.buscar(id);

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificamos que si se encontro la notificacion
        assertEquals("EMAIL", resultado.getTipo()); // Verificamos que los datos no se hayan alterado
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deberia retornar null si la notificacion buscada no existe")
    void buscarNoExistenteTest() {
        // 1. Preparacion (escenario): el repositorio no encuentra nada
        Long idInexistente = 999L;
        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion (When)
        Notificacion resultado = notificacionService.buscar(idInexistente);

        // 3. Verificacion (Then)
        assertNull(resultado); // El servicio usa orElse(null), por lo que debe devolver null
        verify(repository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deberia buscar notificaciones por id de cliente correctamente")
    void buscarPorClienteTest() {
        // 1. Preparacion (escenario)
        Long clienteId = 5L;
        Notificacion n1 = Notificacion.builder().id(1L).clienteId(clienteId).pedidoId(1L)
                .tipo("EMAIL").estado("ENVIADO").leido(false).build();

        when(repository.findByClienteId(clienteId)).thenReturn(Arrays.asList(n1));

        // 2. Ejecucion (When)
        List<Notificacion> resultado = notificacionService.buscarPorCliente(clienteId);

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.stream().allMatch(n -> n.getClienteId().equals(clienteId)));
        verify(repository, times(1)).findByClienteId(clienteId);
    }

    @Test
    @DisplayName("Deberia enviar una notificacion correctamente (simulando WebClient a Cliente y Pedido)")
    void enviarTest() {
        // 1. Preparacion (escenario)
        Notificaciondto dto = new Notificaciondto();
        dto.setClienteId(1L);
        dto.setPedidoId(10L);
        dto.setTipo("EMAIL");
        dto.setAsunto("Tu pedido fue enviado");
        dto.setMensaje("Tu pedido esta en camino");

        Clientedto clienteMock = new Clientedto();
        clienteMock.setId(1L);
        clienteMock.setNombre("Carlos");
        clienteMock.setCorreo("carlos@correo.com");

        Pedidodto pedidoMock = new Pedidodto();
        pedidoMock.setId(10L);
        pedidoMock.setEstado("ENVIADO");
        pedidoMock.setTotal(15000.0);

        // Mock de la cadena de WebClient (Indispensable para que no se quede pegado)
        // El Service llama dos veces a webClient.get()...uri()...retrieve()...bodyToMono(),
        // una vez por cada microservicio (Clientes y Pedidos). Como ambas llamadas comparten
        // la misma forma fluida, encadenamos RequestHeadersUriSpec genericamente.
        WebClient.RequestHeadersUriSpec uriSpecCliente = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecCliente = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecCliente = mock(WebClient.ResponseSpec.class);

        WebClient.RequestHeadersUriSpec uriSpecPedido = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecPedido = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecPedido = mock(WebClient.ResponseSpec.class);

        // get() se llama dos veces; devolvemos primero el "hilo" de cliente y luego el de pedido
        when(webClient.get()).thenReturn(uriSpecCliente, uriSpecPedido);

        // --- Hilo de Cliente ---
        when(uriSpecCliente.uri("http://localhost:8081/clientes/" + dto.getClienteId()))
                .thenReturn(headersSpecCliente);
        when(headersSpecCliente.retrieve()).thenReturn(responseSpecCliente);
        // Aqui es donde entregamos el cliente simulado
        when(responseSpecCliente.bodyToMono(Clientedto.class)).thenReturn(Mono.just(clienteMock));

        // --- Hilo de Pedido ---
        when(uriSpecPedido.uri("http://localhost:8084/pedidos/" + dto.getPedidoId()))
                .thenReturn(headersSpecPedido);
        when(headersSpecPedido.retrieve()).thenReturn(responseSpecPedido);
        // Aqui es donde entregamos el pedido simulado
        when(responseSpecPedido.bodyToMono(Pedidodto.class)).thenReturn(Mono.just(pedidoMock));

        // Simulamos que cuando el repo guarde cualquier notificacion, retorne la misma con ID 1
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion n = invocation.getArgument(0);
            n.setId(1L); // le asignamos manualmente el 1
            return n;
        });

        // 2. Ejecucion (When) Llamas al metodo real de tu logica de negocio
        // El Service consulta primero al microservicio de Clientes, luego al de
        // Pedidos, construye la Notificacion con los datos obtenidos y la guarda.
        Notificacion resultado = notificacionService.enviar(dto);

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvio un nulo
        assertEquals(1L, resultado.getId()); // Compruebas que el ID sea efectivamente el 1 que configuramos
        assertEquals(1L, resultado.getClienteId()); // Verificamos que el clienteId vino del cliente simulado
        assertEquals(10L, resultado.getPedidoId()); // Verificamos que el pedidoId vino del pedido simulado
        assertEquals("EMAIL", resultado.getTipo());
        assertEquals("ENVIADO", resultado.getEstado()); // El servicio fija el estado inicial en "ENVIADO"
        assertFalse(resultado.getLeido()); // El servicio fija leido en false al crear la notificacion
        assertNotNull(resultado.getFechaEnvio()); // Se debe haber asignado la fecha de envio

        // Verificamos que se llamo exactamente 1 vez a cada endpoint externo y al repositorio
        verify(uriSpecCliente, times(1)).uri("http://localhost:8081/clientes/" + dto.getClienteId());
        verify(uriSpecPedido, times(1)).uri("http://localhost:8084/pedidos/" + dto.getPedidoId());
        verify(repository, times(1)).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("Deberia marcar una notificacion como leida correctamente")
    void marcarLeidoTest() {
        // 1. Preparacion (escenario)
        Long id = 1L;
        Notificacion notificacionExistente = Notificacion.builder()
                .id(id).clienteId(1L).pedidoId(1L).tipo("EMAIL")
                .fechaEnvio(LocalDateTime.now()).estado("ENVIADO").leido(false).build();

        when(repository.findById(id)).thenReturn(Optional.of(notificacionExistente));
        // El metodo guarda la misma instancia que recibio, ya modificada
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Ejecucion (When)
        Notificacion resultado = notificacionService.marcarLeido(id);

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertTrue(resultado.getLeido()); // Debe quedar marcada como leida
        assertEquals("LEIDO", resultado.getEstado()); // El estado debe actualizarse a LEIDO
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(notificacionExistente);
    }

    @Test
    @DisplayName("Deberia lanzar excepcion al marcar como leida una notificacion inexistente")
    void marcarLeidoNoExistenteTest() {
        // 1. Preparacion (escenario): el repositorio no encuentra la notificacion
        Long idInexistente = 999L;
        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion y 3. Verificacion (Then)
        // El servicio usa orElseThrow(), por lo que debe lanzar NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> notificacionService.marcarLeido(idInexistente));

        // El repositorio nunca debio llamar a save() porque la excepcion corta el flujo antes
        verify(repository, never()).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("Deberia eliminar una notificacion por id correctamente")
    void eliminarTest() {
        // 1. Preparacion (escenario)
        Long id = 1L;
        // deleteById no retorna nada (void), Mockito no necesita un "when" para esto

        // 2. Ejecucion (When)
        notificacionService.eliminar(id);

        // 3. Verificacion (Then)
        // Verificamos que el repositorio fue llamado exactamente 1 vez con el id correcto
        verify(repository, times(1)).deleteById(id);
    }
}
