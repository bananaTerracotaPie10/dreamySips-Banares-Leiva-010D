package com.dreamysips.service_pedidos.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_pedidos.DTO.ClienteDto;
import com.dreamysips.service_pedidos.DTO.DireccionEnvioDto;
import com.dreamysips.service_pedidos.model.Pedido;
import com.dreamysips.service_pedidos.repository.PedidoRepository;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest 
{

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private DetallePedidoService detallePedidoService;
    @InjectMocks
    private PedidoService pedidoService;

    /**
     * PedidoService tiene un constructor explícito que solo recibe DetallePedidoService.
     * Cuando una clase tiene un constructor declarado, Mockito usa ESE constructor para
     * crear la instancia y deja de inyectar por campo los @Autowired restantes
     * (pedidoRepository y webClientBuilder), por lo que quedan en null si no los
     * inyectamos a mano aquí.
     */
    @BeforeEach
    void setUp ()
    {
        ReflectionTestUtils.setField(pedidoService, "pedidoRepository", pedidoRepository);
        ReflectionTestUtils.setField(pedidoService, "webClientBuilder", webClientBuilder);
    }

    @Test
    @DisplayName ("Debe guardar un pedido correctamente")
    void guardarPedidoTest ()
    {
        // 1. Preparación (escenario)
        Pedido pedido = new Pedido();
        pedido.setRunCliente(11222333L);
        pedido.setEstado("pendiente");
        pedido.setValorTotal(15000);

        // Simulamos que el repo guarde cualquier pedido, retornando el mismo
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // 2. Ejecución (When)
        Pedido resultado = pedidoService.guardar(pedido);

        // 3. Verificación (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvió un nulo
        assertEquals(11222333L, resultado.getRunCliente());
        assertEquals("pendiente", resultado.getEstado());

        // Verificamos que el repositorio fue llamado exactamente 1 vez
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName ("Debe obtener un pedido completo con cliente, direccion y detalles")
    void obtenerPedidoCompletoTest ()
    {
        // --- 1. PREPARACIÓN ---
        Long idPedido = 1L;
        Long runCliente = 11222333L;
        Long idDireccion = 5L;

        // @Value no se resuelve en un test unitario puro, así que lo inyectamos a mano
        ReflectionTestUtils.setField(pedidoService, "clienteUrl", "http://localhost:8081");
        ReflectionTestUtils.setField(pedidoService, "catalogoUrl", "http://localhost:8083");

        Pedido pedidoMock = new Pedido();
        pedidoMock.setIdPedido(idPedido);
        pedidoMock.setRunCliente(runCliente);
        pedidoMock.setIdDireccionEnvio(idDireccion);

        ClienteDto clienteMock = new ClienteDto();
        clienteMock.setRun(runCliente);
        clienteMock.setPrimerNombre("Carlos");

        DireccionEnvioDto direccionMock = new DireccionEnvioDto();
        direccionMock.setIdDireccion(idDireccion);
        direccionMock.setCiudad("Santiago");

        // Mock del repositorio
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoMock));

        // Mock de la cadena de WebClient (Indispensable para que no se quede pegado)
        // Como el Service llama a webClientBuilder.build() dos veces (cliente y direccion),
        // dejamos que cada .build() devuelva su propio WebClient simulado.
        WebClient webClientCliente = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec uriSpecCliente = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecCliente = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecCliente = Mockito.mock(WebClient.ResponseSpec.class);

        WebClient webClientDireccion = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec uriSpecDireccion = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecDireccion = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecDireccion = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClientCliente, webClientDireccion);

        // Cadena para obtener el cliente
        when(webClientCliente.get()).thenReturn(uriSpecCliente);
        when(uriSpecCliente.uri(eq("http://localhost:8081/api/v1/clientes/" + runCliente))).thenReturn(headersSpecCliente);
        when(headersSpecCliente.retrieve()).thenReturn(responseSpecCliente);
        when(responseSpecCliente.bodyToMono(ClienteDto.class)).thenReturn(Mono.just(clienteMock));

        // Cadena para obtener la direccion
        when(webClientDireccion.get()).thenReturn(uriSpecDireccion);
        when(uriSpecDireccion.uri(eq("http://localhost:8081/api/v1/direcciones/" + idDireccion))).thenReturn(headersSpecDireccion);
        when(headersSpecDireccion.retrieve()).thenReturn(responseSpecDireccion);
        when(responseSpecDireccion.bodyToMono(DireccionEnvioDto.class)).thenReturn(Mono.just(direccionMock));

        // El service tambien delega en DetallePedidoService para traer los detalles enriquecidos
        when(detallePedidoService.listarPorPedidoCompleto(idPedido)).thenReturn(Collections.emptyList());

        // --- 2. EJECUCIÓN ---
        Pedido resultado = pedidoService.obtenerPedidoCompleto(idPedido);

        // --- 3. VERIFICACIÓN ---
        assertNotNull(resultado, "El pedido no debe ser nulo");
        assertNotNull(resultado.getDatosCliente(), "Los datos del cliente deben estar presentes");
        assertEquals("Carlos", resultado.getDatosCliente().getPrimerNombre());
        assertNotNull(resultado.getDatosDireccion(), "Los datos de la dirección deben estar presentes");
        assertEquals("Santiago", resultado.getDatosDireccion().getCiudad());

        List<?> detalles = resultado.getDetalles();
        assertNotNull(detalles, "La lista de detalles no debe ser nula");
        assertEquals(0, detalles.size());

        // Verificamos que se llamó al repositorio y al service de detalles exactamente 1 vez
        verify(pedidoRepository, times(1)).findById(idPedido);
        verify(detallePedidoService, times(1)).listarPorPedidoCompleto(idPedido);
    }

}