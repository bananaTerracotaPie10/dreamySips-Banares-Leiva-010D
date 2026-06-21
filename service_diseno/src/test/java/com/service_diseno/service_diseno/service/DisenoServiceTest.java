package com.service_diseno.service_diseno.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.service_diseno.service_diseno.model.Diseno;
import com.service_diseno.service_diseno.repository.DisenoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Usamos Mockito para simular objetos
class DisenoServiceTest {

    @Mock
    private DisenoRepository disenoRepository; // Simulamos el repositorio

    @InjectMocks
    private DisenoService disenoService; // Inyectamos el mock en el servicio real

    @Test
    @DisplayName("Deberia listar todos los disenos correctamente")
    void listarTodosTest() {
        // 1. Preparacion (escenario)
        Diseno diseno1 = new Diseno(1L, "Grande", true, 15000, "foto1.png", "dibujo1.png", 10L);
        Diseno diseno2 = new Diseno(2L, "Mediano", false, 10000, "foto2.png", "dibujo2.png", 11L);

        when(disenoRepository.findAll()).thenReturn(Arrays.asList(diseno1, diseno2));

        // 2. Ejecucion (When)
        List<Diseno> resultado = disenoService.listarTodos();

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificamos que el servicio no devolvio un nulo
        assertEquals(2, resultado.size()); // Comprobamos que la cantidad de disenos sea la esperada
        verify(disenoRepository, times(1)).findAll(); // Verificamos que el repositorio fue llamado exactamente 1 vez
    }

    @Test
    @DisplayName("Deberia buscar un diseno por id correctamente")
    void buscarPorIdTest() {
        // 1. Preparacion (escenario)
        Long idDiseno = 1L;
        Diseno disenoMock = new Diseno(idDiseno, "Grande", true, 15000, "foto1.png", "dibujo1.png", 10L);

        // Simulamos que el repositorio retorna el diseno envuelto en un Optional
        when(disenoRepository.findById(idDiseno)).thenReturn(Optional.of(disenoMock));

        // 2. Ejecucion (When)
        Optional<Diseno> resultado = disenoService.buscarPorId(idDiseno);

        // 3. Verificacion (Then)
        assertTrue(resultado.isPresent()); // Verificamos que si se encontro el diseno
        assertEquals("Grande", resultado.get().getTamano()); // Verificamos que los datos no se hayan alterado
        verify(disenoRepository, times(1)).findById(idDiseno);
    }

    @Test
    @DisplayName("Deberia guardar un diseno correctamente cuando tiene un detalle asociado")
    void guardarDisenoTest() {
        // 1. Preparacion (escenario)
        Diseno diseno = new Diseno();
        diseno.setTamano("Grande");
        diseno.setAColor(true);
        diseno.setPrecio(15000);
        diseno.setIdDetalle(10L); // El detalle es obligatorio, lo definimos aqui

        // Simulamos que cuando el repo guarde cualquier diseno, retorne el mismo con ID 1
        // Como no queremos usar una base de datos real, le decimos a Mockito:
        // "Cuando alguien llame al metodo .save() del repositorio con cualquier objeto de tipo Diseno
        // (any(Diseno.class))..."
        when(disenoRepository.save(any(Diseno.class))).thenAnswer(invocation -> {
            Diseno d = invocation.getArgument(0);
            d.setIdDiseno(1L); // le asignamos manualmente el 1
            return d;
        });

        // 2. Ejecucion (When) Llamas al metodo real de tu logica de negocio
        // Estas ejecutando el codigo que tu escribiste en el Service. El Service,
        // internamente, llamara al disenoRepository.save(). Como configuraste el
        // "simulacro" en el paso anterior, el Service recibira el diseno con el ID 1.
        Diseno resultado = disenoService.guardar(diseno);

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvio un nulo
        assertEquals(1L, resultado.getIdDiseno()); // Compruebas que el ID sea efectivamente el 1 que configuramos
        // en el simulacro. Esto confirma que el flujo de datos paso por el repositorio.
        assertEquals("Grande", resultado.getTamano()); // Verificas que los datos no se hayan alterado en el camino.
        // Verificamos que el repositorio fue llamado exactamente 1 vez. Si tu codigo
        // llamara al repositorio dos veces por error, el test fallaria.
        verify(disenoRepository, times(1)).save(diseno);
    }

    @Test
    @DisplayName("No deberia guardar un diseno si no tiene un detalle asociado")
    void guardarDisenoSinDetalleTest() {
        // 1. Preparacion (escenario): un diseno sin idDetalle
        Diseno diseno = new Diseno();
        diseno.setTamano("Grande");
        diseno.setAColor(true);
        diseno.setPrecio(15000);
        diseno.setIdDetalle(null); // Sin detalle asociado, debe fallar la regla de negocio

        // 2. Ejecucion y 3. Verificacion (Then)
        // Verificamos que el servicio lance la excepcion esperada antes de tocar el repositorio
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> disenoService.guardar(diseno));

        assertEquals("El diseño debe tener un detalle asociado", excepcion.getMessage());
        // El repositorio nunca debio ser invocado porque la validacion corta el flujo antes
        verify(disenoRepository, never()).save(any(Diseno.class));
    }

    @Test
    @DisplayName("Deberia eliminar un diseno por id correctamente")
    void eliminarDisenoTest() {
        // 1. Preparacion (escenario)
        Long idDiseno = 1L;
        // deleteById no retorna nada (void), Mockito no necesita un "when" para esto

        // 2. Ejecucion (When)
        disenoService.eliminar(idDiseno);

        // 3. Verificacion (Then)
        // Verificamos que el repositorio fue llamado exactamente 1 vez con el id correcto
        verify(disenoRepository, times(1)).deleteById(idDiseno);
    }

    @Test
    @DisplayName("Deberia buscar disenos por id de detalle correctamente")
    void buscarPorDetalleTest() {
        // 1. Preparacion (escenario)
        Long idDetalle = 10L;
        Diseno diseno1 = new Diseno(1L, "Grande", true, 15000, "foto1.png", "dibujo1.png", idDetalle);
        Diseno diseno2 = new Diseno(2L, "Pequeno", false, 8000, "foto2.png", "dibujo2.png", idDetalle);

        when(disenoRepository.findByIdDetalle(idDetalle)).thenReturn(Arrays.asList(diseno1, diseno2));

        // 2. Ejecucion (When)
        List<Diseno> resultado = disenoService.buscarPorDetalle(idDetalle);

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        // Verificamos que todos los disenos retornados pertenecen al detalle solicitado
        assertTrue(resultado.stream().allMatch(d -> d.getIdDetalle().equals(idDetalle)));
        verify(disenoRepository, times(1)).findByIdDetalle(idDetalle);
    }

    @Test
    @DisplayName("Deberia listar solo los disenos a color")
    void listarDisenosColorTest() {
        // 1. Preparacion (escenario)
        Diseno disenoColor = new Diseno(1L, "Grande", true, 15000, "foto1.png", "dibujo1.png", 10L);

        when(disenoRepository.buscarDisenosColor()).thenReturn(Arrays.asList(disenoColor));

        // 2. Ejecucion (When)
        List<Diseno> resultado = disenoService.listarDisenosColor();

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getAColor()); // Verificamos que efectivamente sea "a color"
        verify(disenoRepository, times(1)).buscarDisenosColor();
    }

    @Test
    @DisplayName("Deberia retornar un Optional vacio si el diseno no existe")
    void buscarPorIdNoExistenteTest() {
        // 1. Preparacion (escenario): el repositorio no encuentra nada
        Long idInexistente = 999L;
        when(disenoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion (When)
        Optional<Diseno> resultado = disenoService.buscarPorId(idInexistente);

        // 3. Verificacion (Then)
        assertFalse(resultado.isPresent()); // No debe haber diseno presente
        verify(disenoRepository, times(1)).findById(idInexistente);
    }
}
