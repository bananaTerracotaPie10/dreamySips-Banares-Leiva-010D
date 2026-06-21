package com.dreamysips.service_pago.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dreamysips.service_pago.model.Pago;
import com.dreamysips.service_pago.model.TransaccionPago;
import com.dreamysips.service_pago.repository.PagoRepository;
import com.dreamysips.service_pago.repository.TransaccionPagoRepository;

@ExtendWith(MockitoExtension.class)
public class TransaccionPagoServiceTest 
{

    @Mock
    private TransaccionPagoRepository transaccionPagoRepository;
    @Mock
    private PagoRepository pagoRepository;
    @InjectMocks
    private TransaccionPagoService transaccionPagoService;

    @Test
    @DisplayName ("Debe guardar una transaccion vinculada a un pago existente")
    void guardarTransaccionTest ()
    {
        // 1. Preparación (escenario)
        Long idPago = 1L;

        Pago pago = new Pago();
        pago.setIdPago(idPago);

        TransaccionPago transaccion = new TransaccionPago();
        transaccion.setTipoTransaccion("cobro");
        transaccion.setMontoTransaccion(10000);
        transaccion.setEstadoTransaccion("aprobado");

        // Simulamos que el pago existe
        when(pagoRepository.findById(idPago)).thenReturn(Optional.of(pago));
        // Simulamos que el repo guarde cualquier transaccion, retornando la misma
        when(transaccionPagoRepository.save(any(TransaccionPago.class))).thenReturn(transaccion);

        // 2. Ejecución (When)
        TransaccionPago resultado = transaccionPagoService.guardar(idPago, transaccion);

        // 3. Verificación (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvió un nulo
        assertEquals("cobro", resultado.getTipoTransaccion());
        assertEquals(10000, resultado.getMontoTransaccion());
        assertEquals(pago, resultado.getPagos()); // Confirma que se vinculó el pago correcto

        // Verificamos que ambos repositorios fueron llamados exactamente 1 vez
        verify(pagoRepository, times(1)).findById(idPago);
        verify(transaccionPagoRepository, times(1)).save(transaccion);
    }

    @Test
    @DisplayName ("guardar debe lanzar excepcion si el pago no existe")
    void guardarTransaccionConPagoInexistenteTest ()
    {
        // 1. Preparación (escenario): el repo de pagos no encuentra nada
        Long idPago = 99L;
        TransaccionPago transaccion = new TransaccionPago();

        when(pagoRepository.findById(idPago)).thenReturn(Optional.empty());

        // 2. Ejecución y 3. Verificación: se espera que lance RuntimeException
        assertThrows(RuntimeException.class, () -> transaccionPagoService.guardar(idPago, transaccion));

        // El repositorio de transacciones nunca debió llamarse, ya que el flujo se interrumpe antes con el orElseThrow
        verify(transaccionPagoRepository, times(0)).save(any(TransaccionPago.class));
    }

    @Test
    @DisplayName ("Debe vincular una transaccion existente con un pago existente")
    void vincularConIdTransaccionTest ()
    {
        // 1. Preparación (escenario)
        Long idPago = 1L;
        Long idTransaccion = 10L;

        Pago pago = new Pago();
        pago.setIdPago(idPago);

        TransaccionPago transaccion = new TransaccionPago();
        transaccion.setIdTransaccion(idTransaccion);
        transaccion.setTipoTransaccion("reembolso");

        when(pagoRepository.findById(idPago)).thenReturn(Optional.of(pago));
        when(transaccionPagoRepository.findById(idTransaccion)).thenReturn(Optional.of(transaccion));
        when(transaccionPagoRepository.save(any(TransaccionPago.class))).thenReturn(transaccion);

        // 2. Ejecución (When)
        TransaccionPago resultado = transaccionPagoService.vincularConIdTransaccion(idPago, idTransaccion);

        // 3. Verificación (Then)
        assertNotNull(resultado);
        assertEquals(pago, resultado.getPagos()); // Confirma que quedó vinculada al pago correcto
        assertEquals("reembolso", resultado.getTipoTransaccion());

        verify(pagoRepository, times(1)).findById(idPago);
        verify(transaccionPagoRepository, times(1)).findById(idTransaccion);
        verify(transaccionPagoRepository, times(1)).save(transaccion);
    }

    @Test
    @DisplayName ("vincularConIdTransaccion debe lanzar excepcion si la transaccion no existe")
    void vincularConIdTransaccionInexistenteTest ()
    {
        // 1. Preparación (escenario): el pago existe, pero la transaccion no
        Long idPago = 1L;
        Long idTransaccion = 999L;

        Pago pago = new Pago();
        pago.setIdPago(idPago);

        when(pagoRepository.findById(idPago)).thenReturn(Optional.of(pago));
        when(transaccionPagoRepository.findById(idTransaccion)).thenReturn(Optional.empty());

        // 2. Ejecución y 3. Verificación: se espera que lance RuntimeException
        assertThrows(RuntimeException.class,
                () -> transaccionPagoService.vincularConIdTransaccion(idPago, idTransaccion));

        verify(transaccionPagoRepository, times(0)).save(any(TransaccionPago.class));
    }

}
