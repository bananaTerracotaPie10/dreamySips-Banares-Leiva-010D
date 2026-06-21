package com.dreamysips.service_cliente.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.dreamysips.service_cliente.model.DireccionEnvio;
import com.dreamysips.service_cliente.repository.DireccionEnvioRepository;

@ExtendWith(MockitoExtension.class)
public class DireccionEnvioServiceTest 
{

    @Mock
    private DireccionEnvioRepository direccionEnvioRepository;
    @InjectMocks
    private DireccionEnvioService direccionEnvioService;

    @Test
    @DisplayName ("Debe guardar una direccion de envio correctamente")
    void guardarDireccionEnvioTest ()
    {
        // 1. Preparación (escenario)
        DireccionEnvio direccion = new DireccionEnvio();
        direccion.setCiudad("Santiago");
        direccion.setComuna("Peñaflor");
        direccion.setCalle("Av. Siempre Viva");
        direccion.setNumero("123");

        // Simulamos que cuando el repo guarde cualquier direccion, retorne la misma
        // "Cuando alguien llame al método .save() del repositorio con cualquier objeto de tipo DireccionEnvio..."
        when(direccionEnvioRepository.save(any(DireccionEnvio.class))).thenReturn(direccion);

        // 2. Ejecución (When) Llamas al método real de tu lógica de negocio
        // El Service llamará internamente a direccionEnvioRepository.save(), que ya
        // está simulado en el paso anterior.
        DireccionEnvio resultado = direccionEnvioService.guardar(direccion);

        // 3. Verificación (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvió un nulo
        assertEquals("Santiago", resultado.getCiudad()); // Comprueba que los datos no se alteraron
        assertEquals("Peñaflor", resultado.getComuna());
        assertEquals("Av. Siempre Viva", resultado.getCalle());
        assertEquals("123", resultado.getNumero());

        // Verificamos que el repositorio fue llamado exactamente 1 vez
        verify(direccionEnvioRepository, times(1)).save(direccion);
    }

}
