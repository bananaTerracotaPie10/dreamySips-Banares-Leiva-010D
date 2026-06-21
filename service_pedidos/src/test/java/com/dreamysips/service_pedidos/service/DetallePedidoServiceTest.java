package com.dreamysips.service_pedidos.service;

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

import com.dreamysips.service_pedidos.DTO.ProductoDto;
import com.dreamysips.service_pedidos.model.DetallePedido;
import com.dreamysips.service_pedidos.model.Pedido;
import com.dreamysips.service_pedidos.repository.DetallePedidoRepository;
import com.dreamysips.service_pedidos.repository.PedidoRepository;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class DetallePedidoServiceTest 
{

    @Mock
    private DetallePedidoRepository detallePedidoRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private WebClient.Builder webClientBuilder;
    @InjectMocks
    private DetallePedidoService detallePedidoService;

    @Test
    @DisplayName ("Debe guardar un detalle vinculado a un pedido existente")
    void guardarDetalleTest ()
    {
        // 1. Preparación (escenario)
        Long idPedido = 1L;

        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);

        DetallePedido detalle = new DetallePedido();
        detalle.setIdProducto(50L);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(3000);

        // Simulamos que el pedido existe
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));
        // Simulamos que el repo guarde cualquier detalle, retornando el mismo
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(detalle);

        // 2. Ejecución (When)
        DetallePedido resultado = detallePedidoService.guardar(idPedido, detalle);

        // 3. Verificación (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvió un nulo
        assertEquals(2, resultado.getCantidad());
        assertEquals(pedido, resultado.getPedido()); // Confirma que se vinculó el pedido correcto

        // Verificamos que ambos repositorios fueron llamados exactamente 1 vez
        verify(pedidoRepository, times(1)).findById(idPedido);
        verify(detallePedidoRepository, times(1)).save(detalle);
    }

    @Test
    @DisplayName ("Debe obtener un detalle completo con los datos del producto")
    void obtenerDetalleCompletoTest ()
    {
        // --- 1. PREPARACIÓN ---
        Long idDetalle = 1L;
        Long idProducto = 50L;

        // @Value no se resuelve en un test unitario puro, así que lo inyectamos a mano
        ReflectionTestUtils.setField(detallePedidoService, "catalogoUrl", "http://localhost:8083");

        DetallePedido detalleMock = new DetallePedido();
        detalleMock.setIdDetalle(idDetalle);
        detalleMock.setIdProducto(idProducto);
        detalleMock.setCantidad(3);

        ProductoDto productoMock = new ProductoDto();
        productoMock.setIdProducto(idProducto);
        productoMock.setDescripcion("Sirope de vainilla");
        productoMock.setValor(2500);

        // Mock del repositorio
        when(detallePedidoRepository.findById(idDetalle)).thenReturn(Optional.of(detalleMock));

        // Mock de la cadena de WebClient (Indispensable para que no se quede pegado)
        WebClient webClient = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec uriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        // Aquí es donde entregamos el producto simulado
        when(responseSpec.bodyToMono(ProductoDto.class)).thenReturn(Mono.just(productoMock));

        // --- 2. EJECUCIÓN ---
        DetallePedido resultado = detallePedidoService.obtenerDetalleCompleto(idDetalle);

        // --- 3. VERIFICACIÓN ---
        assertNotNull(resultado, "El detalle no debe ser nulo");
        assertNotNull(resultado.getDatosProducto(), "Los datos del producto deben estar presentes");
        assertEquals("Sirope de vainilla", resultado.getDatosProducto().getDescripcion());
        assertEquals(idProducto, resultado.getDatosProducto().getIdProducto());

        // Verificamos que se llamó al repositorio
        verify(detallePedidoRepository, times(1)).findById(idDetalle);
    }

}
