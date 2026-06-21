package com.dreamysips.service_despacho.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_despacho.dto.ClienteDTO;
import com.dreamysips.service_despacho.dto.PedidoDTO;
import com.dreamysips.service_despacho.model.Despacho;
import com.dreamysips.service_despacho.repository.DespachoRepository;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Usamos Mockito para simular objetos
class DespachoServiceTest {

    @Mock
    private DespachoRepository repository; // Simulamos el repositorio

    // El WebClient real no se simula con @Mock + @InjectMocks porque el Service
    // recibe un WebClient ya construido (no un WebClient.Builder). Lo simulamos
    // manualmente con Mockito.mock() y lo inyectamos por constructor.
    private WebClient webClient;

    private DespachoService despachoService;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        despachoService = new DespachoService(repository, webClient);
    }

    @Test
    @DisplayName("Deberia listar todos los despachos correctamente")
    void listarTest() {
        // 1. Preparacion (escenario)
        Despacho d1 = Despacho.builder().id(1L).pedidoId(1L).clienteId(1L)
                .estado("PREPARANDO").codigoSeguimiento("ABC-1").build();
        Despacho d2 = Despacho.builder().id(2L).pedidoId(2L).clienteId(2L)
                .estado("EN_TRANSITO").codigoSeguimiento("ABC-2").build();

        when(repository.findAll()).thenReturn(Arrays.asList(d1, d2));

        // 2. Ejecucion (When)
        List<Despacho> resultado = despachoService.listar();

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificamos que el servicio no devolvio un nulo
        assertEquals(2, resultado.size()); // Comprobamos la cantidad esperada
        verify(repository, times(1)).findAll(); // Verificamos que el repositorio fue llamado exactamente 1 vez
    }

    @Test
    @DisplayName("Deberia buscar un despacho por id correctamente")
    void buscarTest() {
        // 1. Preparacion (escenario)
        Long id = 1L;
        Despacho despachoMock = Despacho.builder().id(id).pedidoId(1L).clienteId(1L)
                .estado("PREPARANDO").codigoSeguimiento("ABC-1").build();

        when(repository.findById(id)).thenReturn(Optional.of(despachoMock));

        // 2. Ejecucion (When)
        Despacho resultado = despachoService.buscar(id);

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificamos que si se encontro el despacho
        assertEquals("PREPARANDO", resultado.getEstado()); // Verificamos que los datos no se hayan alterado
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deberia lanzar excepcion si el despacho buscado no existe")
    void buscarNoExistenteTest() {
        // 1. Preparacion (escenario): el repositorio no encuentra nada
        Long idInexistente = 999L;
        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion y 3. Verificacion (Then)
        // El servicio usa orElseThrow(), por lo que debe lanzar RuntimeException
        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> despachoService.buscar(idInexistente));

        assertEquals("Despacho no encontrado", excepcion.getMessage());
        verify(repository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deberia buscar despachos por id de cliente correctamente")
    void buscarPorClienteTest() {
        // 1. Preparacion (escenario)
        Long clienteId = 5L;
        Despacho d1 = Despacho.builder().id(1L).pedidoId(1L).clienteId(clienteId)
                .estado("PREPARANDO").codigoSeguimiento("ABC-1").build();

        when(repository.findByClienteId(clienteId)).thenReturn(Arrays.asList(d1));

        // 2. Ejecucion (When)
        List<Despacho> resultado = despachoService.buscarPorCliente(clienteId);

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.stream().allMatch(d -> d.getClienteId().equals(clienteId)));
        verify(repository, times(1)).findByClienteId(clienteId);
    }

    @Test
    @DisplayName("Deberia buscar un despacho por codigo de seguimiento correctamente")
    void buscarPorCodigoTest() {
        // 1. Preparacion (escenario)
        String codigo = "ABC-123";
        Despacho despachoMock = Despacho.builder().id(1L).pedidoId(1L).clienteId(1L)
                .estado("EN_TRANSITO").codigoSeguimiento(codigo).build();

        when(repository.findByCodigoSeguimiento(codigo)).thenReturn(Optional.of(despachoMock));

        // 2. Ejecucion (When)
        Despacho resultado = despachoService.buscarPorCodigo(codigo);

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertEquals(codigo, resultado.getCodigoSeguimiento());
        verify(repository, times(1)).findByCodigoSeguimiento(codigo);
    }

    @Test
    @DisplayName("Deberia lanzar excepcion si el codigo de seguimiento no existe")
    void buscarPorCodigoNoExistenteTest() {
        // 1. Preparacion (escenario): el repositorio no encuentra el codigo
        String codigoInexistente = "NO-EXISTE";
        when(repository.findByCodigoSeguimiento(codigoInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion y 3. Verificacion (Then)
        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> despachoService.buscarPorCodigo(codigoInexistente));

        assertEquals("Código no encontrado", excepcion.getMessage());
        verify(repository, times(1)).findByCodigoSeguimiento(codigoInexistente);
    }

    @Test
    @DisplayName("Deberia crear un despacho correctamente (simulando WebClient a Pedido y Cliente)")
    void crearDespachoTest() {
        // 1. Preparacion (escenario)
        Long pedidoId = 100L;
        Long clienteId = 1L;

        PedidoDTO pedidoMock = new PedidoDTO();
        pedidoMock.setId(pedidoId);
        pedidoMock.setClienteId(clienteId);
        pedidoMock.setEstado("CONFIRMADO");
        pedidoMock.setTotal(15000.0);

        ClienteDTO clienteMock = new ClienteDTO();
        clienteMock.setId(clienteId);
        clienteMock.setNombre("Carlos");
        clienteMock.setCorreo("carlos@correo.com");

        // Mock de la cadena de WebClient (Indispensable para que no se quede pegado)
        // El Service llama primero a Pedidos y luego a Clientes (con el clienteId
        // que vino del pedido). Como ambas llamadas comparten la misma forma fluida,
        // creamos un "hilo" mock independiente por cada llamada.
        WebClient.RequestHeadersUriSpec uriSpecPedido = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecPedido = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecPedido = mock(WebClient.ResponseSpec.class);

        WebClient.RequestHeadersUriSpec uriSpecCliente = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecCliente = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecCliente = mock(WebClient.ResponseSpec.class);

        // get() se llama dos veces; devolvemos primero el "hilo" de pedido y luego el de cliente
        when(webClient.get()).thenReturn(uriSpecPedido, uriSpecCliente);

        // --- Hilo de Pedido ---
        when(uriSpecPedido.uri("http://localhost:8085/pedidos/" + pedidoId))
                .thenReturn(headersSpecPedido);
        when(headersSpecPedido.retrieve()).thenReturn(responseSpecPedido);
        // Aqui es donde entregamos el pedido simulado
        when(responseSpecPedido.bodyToMono(PedidoDTO.class)).thenReturn(Mono.just(pedidoMock));

        // --- Hilo de Cliente ---
        when(uriSpecCliente.uri("http://localhost:8082/clientes/" + clienteId))
                .thenReturn(headersSpecCliente);
        when(headersSpecCliente.retrieve()).thenReturn(responseSpecCliente);
        // Aqui es donde entregamos el cliente simulado
        when(responseSpecCliente.bodyToMono(ClienteDTO.class)).thenReturn(Mono.just(clienteMock));

        // Simulamos que cuando el repo guarde cualquier despacho, retorne el mismo con ID 1
        when(repository.save(any(Despacho.class))).thenAnswer(invocation -> {
            Despacho d = invocation.getArgument(0);
            d.setId(1L); // le asignamos manualmente el 1
            return d;
        });

        // 2. Ejecucion (When) Llamas al metodo real de tu logica de negocio
        // El Service consulta primero al microservicio de Pedidos, luego al de
        // Clientes (usando el clienteId del pedido), construye el Despacho con
        // estado inicial "PREPARANDO" y lo guarda.
        Despacho resultado = despachoService.crearDespacho(pedidoId);

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvio un nulo
        assertEquals(1L, resultado.getId()); // Compruebas que el ID sea efectivamente el 1 que configuramos
        assertEquals(pedidoId, resultado.getPedidoId()); // Verificamos que el pedidoId sea el solicitado
        assertEquals(clienteId, resultado.getClienteId()); // Verificamos que el clienteId vino del cliente simulado
        assertEquals("PREPARANDO", resultado.getEstado()); // El servicio fija el estado inicial en "PREPARANDO"
        assertEquals(LocalDate.now(), resultado.getFechaDespacho()); // Se debe haber asignado la fecha de despacho
        assertEquals(LocalDate.now().plusDays(5), resultado.getFechaEstimada()); // Estimada = hoy + 5 dias
        assertNotNull(resultado.getCodigoSeguimiento()); // Debe haberse generado un codigo de seguimiento (UUID)

        // Verificamos que se llamo exactamente 1 vez a cada endpoint externo y al repositorio
        verify(uriSpecPedido, times(1)).uri("http://localhost:8085/pedidos/" + pedidoId);
        verify(uriSpecCliente, times(1)).uri("http://localhost:8082/clientes/" + clienteId);
        verify(repository, times(1)).save(any(Despacho.class));
    }

    @Test
    @DisplayName("Deberia actualizar el estado sin notificar si el nuevo estado no es EN_TRANSITO")
    void actualizarEstadoSinNotificarTest() {
        // 1. Preparacion (escenario)
        Long id = 1L;
        Despacho despachoExistente = Despacho.builder().id(id).pedidoId(1L).clienteId(1L)
                .estado("PREPARANDO").codigoSeguimiento("ABC-1").build();

        when(repository.findById(id)).thenReturn(Optional.of(despachoExistente));
        when(repository.save(any(Despacho.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Ejecucion (When)
        Despacho resultado = despachoService.actualizarEstado(id, "CANCELADO");

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertEquals("CANCELADO", resultado.getEstado());
        verify(repository, times(1)).save(despachoExistente);
        // Como el estado no es EN_TRANSITO, el Service nunca debe llamar a webClient.post()
        verify(webClient, never()).post();
    }

    @Test
    @DisplayName("Deberia actualizar el estado a EN_TRANSITO y notificar al cliente via WebClient")
    void actualizarEstadoEnTransitoTest() {
        // 1. Preparacion (escenario)
        Long id = 1L;
        Despacho despachoExistente = Despacho.builder().id(id).pedidoId(1L).clienteId(1L)
                .estado("PREPARANDO").codigoSeguimiento("ABC-1").build();

        when(repository.findById(id)).thenReturn(Optional.of(despachoExistente));
        when(repository.save(any(Despacho.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock de la cadena de WebClient para el POST de notificacion
        // (Indispensable para que no se quede pegado)
        WebClient.RequestBodyUriSpec bodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri("http://localhost:8086/notificaciones/enviar")).thenReturn(bodyUriSpec);
        when(bodyUriSpec.bodyValue(any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        // El POST de notificacion no devuelve cuerpo util, simulamos un Mono<Void> vacio
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // 2. Ejecucion (When)
        Despacho resultado = despachoService.actualizarEstado(id, "EN_TRANSITO");

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertEquals("EN_TRANSITO", resultado.getEstado());
        verify(repository, times(1)).save(despachoExistente);
        // Verificamos que se notifico exactamente 1 vez al microservicio de notificaciones
        verify(webClient, times(1)).post();
        verify(bodyUriSpec, times(1)).uri("http://localhost:8086/notificaciones/enviar");
    }

    @Test
    @DisplayName("Deberia confirmar la entrega de un despacho correctamente")
    void confirmarEntregaTest() {
        // 1. Preparacion (escenario)
        Long id = 1L;
        Despacho despachoExistente = Despacho.builder().id(id).pedidoId(1L).clienteId(1L)
                .estado("EN_TRANSITO").codigoSeguimiento("ABC-1").build();

        when(repository.findById(id)).thenReturn(Optional.of(despachoExistente));
        when(repository.save(any(Despacho.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Ejecucion (When)
        Despacho resultado = despachoService.confirmarEntrega(id);

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertEquals("ENTREGADO", resultado.getEstado()); // El estado debe quedar como ENTREGADO
        assertEquals(LocalDate.now(), resultado.getFechaEntrega()); // Se debe asignar la fecha de entrega de hoy
        verify(repository, times(1)).save(despachoExistente);
    }

    @Test
    @DisplayName("Deberia eliminar un despacho por id correctamente")
    void eliminarTest() {
        // 1. Preparacion (escenario)
        Long id = 1L;
        // deleteById no retorna nada (void), Mockito no necesita un "when" para esto

        // 2. Ejecucion (When)
        despachoService.eliminar(id);

        // 3. Verificacion (Then)
        // Verificamos que el repositorio fue llamado exactamente 1 vez con el id correcto
        verify(repository, times(1)).deleteById(id);
    }
}
