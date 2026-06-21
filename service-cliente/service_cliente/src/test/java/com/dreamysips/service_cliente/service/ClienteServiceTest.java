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

import com.dreamysips.service_cliente.model.Cliente;
import com.dreamysips.service_cliente.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest 
{

    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private ClienteService clienteService;
    @Test
    @DisplayName ("Debe guardar un cliente correctamente")

    void guardarClienteTest ()
    {
        Cliente cliente = new Cliente();
        cliente.setRun(11222333L);
        cliente.setPrimerNombre("Carlos");
        cliente.setSegundoNombre("El Topo");
        cliente.setApPaterno("Que Gira");
        cliente.setApMaterno("Silva");
        cliente.setCorreo("carlosGiraTopo@correo.com");
        cliente.setTelefono("600 500 700");

        // Simulamos que cuando el repo guarde cualquier cliente, retorne el mismo objeto
        // Como no queremos usar una base de datos real, le decimos a Mockito:
        // "Cuando alguien llame al método .save() del repositorio con cualquier objeto de tipo Cliente..."
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // 2. Ejecución (When) Llamas al método real de tu lógica de negocio
        // Estás ejecutando el código que tú escribiste en el Service. El Service,
        // internamente, llamará al clienteRepository.save(). Como configuramos el
        // "simulacro" en el paso anterior, el Service recibirá el cliente guardado.
        Cliente resultado = clienteService.guardar(cliente);

        // 3. Verificación (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvió un nulo
        assertEquals(11222333L, resultado.getRun()); // Comprueba que el run sea el esperado
        assertEquals("Carlos", resultado.getPrimerNombre()); // Verificas que los datos no se hayan alterado en el camino
        assertEquals("El Topo", resultado.getSegundoNombre()); 
        assertEquals("Que Gira", resultado.getApPaterno()); 
        assertEquals("carlosGiraTopo@correo.com", resultado.getCorreo()); 
        assertEquals("600 500 700", resultado.getTelefono()); 

        // Verifica que el repositorio fue llamado exactamente 1 vez. 
        // si  el cod llama al repositorio dos veces por error, el test fallaría.
        verify(clienteRepository, times(1)).save(cliente);
    }
    



}
