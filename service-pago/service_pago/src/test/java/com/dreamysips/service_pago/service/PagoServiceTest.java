package com.dreamysips.service_pago.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamysips.service_pago.dto.PedidoDto;
import com.dreamysips.service_pago.model.Pago;
import com.dreamysips.service_pago.repository.PagoRepository;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest 
{

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private WebClient.Builder webClientBuilder;
    @InjectMocks
    private PagoService pagoService;

    @Test
    @DisplayName ("Debe guardar un pago correctamente")
    void guardarPagoTest ()
    {
        // 1. Preparación (escenario)
        Pago pago = new Pago();
        pago.setIdPedido(1L);
        pago.setMetodo("debito");
        pago.setMonto(5000);
        pago.setEstado("aprobado");

        // Simulamos que cuando el repo guarde cualquier pago, retorne el mismo
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // 2. Ejecución (When)
        Pago resultado = pagoService.guardar(pago);

        // 3. Verificación (Then)
        assertNotNull(resultado); // Verifica que el servicio no devolvió un nulo
        assertEquals("debito", resultado.getMetodo()); // Comprueba que los datos no se alteraron
        assertEquals(5000, resultado.getMonto());

        // Verificamos que el repositorio fue llamado exactamente 1 vez
        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    @DisplayName ("Debe obtener un pago con los datos externos del pedido")
    void obtenerPagoConDatosExternosTest ()
    {
        // --- 1. PREPARACIÓN ---
        Long idPago = 1L;
        Long idPedido = 100L;

        // Necesitamos inyectar el valor de @Value ("${services.pedidos.url}") manualmente,
        // ya que en un test unitario puro Spring no levanta el contexto para resolverlo.
        ReflectionTestUtils.setField(pagoService, "pedidoUrl", "http://localhost:8082");

        Pago pagoMock = new Pago();
        pagoMock.setIdPago(idPago);
        pagoMock.setIdPedido(idPedido);
        pagoMock.setMetodo("debito");

        PedidoDto pedidoMock = new PedidoDto();
        pedidoMock.setIdPedido(idPedido);
        pedidoMock.setRunCliente("11222333-4");
        pedidoMock.setEstado("confirmado");

        // Mock del repositorio
        when(pagoRepository.findById(idPago)).thenReturn(Optional.of(pagoMock));

        // Mock de la cadena de WebClient (Indispensable pa que no se quede pegado)
        WebClient webClient = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec uriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        // Aquí es donde entregamos el pedido simulado
        when(responseSpec.bodyToMono(PedidoDto.class)).thenReturn(Mono.just(pedidoMock));

        // --- 2. EJECUCIÓN ---
        Pago resultado = pagoService.obtenerPagoConDatosExternos(idPago);

        // --- 3. VERIFICACIÓN ---
        assertNotNull(resultado, "El pago no debe ser nulo");
        assertNotNull(resultado.getDatosPedido(), "Los datos del pedido deben estar presentes");
        assertEquals("11222333-4", resultado.getDatosPedido().getRunCliente(), "El run del cliente debe coincidir con el simulado");
        assertEquals(idPedido, resultado.getDatosPedido().getIdPedido());

        // Verificamos que se llamó al repositorio
        verify(pagoRepository, times(1)).findById(idPago);
    }

}
