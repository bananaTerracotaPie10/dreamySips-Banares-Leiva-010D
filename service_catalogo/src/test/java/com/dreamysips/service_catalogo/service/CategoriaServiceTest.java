package com.dreamysips.service_catalogo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dreamysips.service_catalogo.model.Categoria;
import com.dreamysips.service_catalogo.repository.CategoriaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Usamos Mockito para simular objetos
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository; // Simulamos el repositorio

    @InjectMocks
    private CategoriaService categoriaService; // Inyectamos el mock en el servicio real

    @Test
    @DisplayName("Deberia listar todas las categorias correctamente")
    void listarTodasTest() {
        // 1. Preparacion (escenario)
        Categoria c1 = new Categoria(1L, "Tazas", "Tazas personalizadas", null);
        Categoria c2 = new Categoria(2L, "Poleras", "Poleras personalizadas", null);

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        // 2. Ejecucion (When)
        List<Categoria> resultado = categoriaService.listarTodas();

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificamos que el servicio no devolvio un nulo
        assertEquals(2, resultado.size()); // Comprobamos la cantidad esperada
        verify(categoriaRepository, times(1)).findAll(); // Verificamos que el repositorio fue llamado exactamente 1 vez
    }

    @Test
    @DisplayName("Deberia buscar una categoria por id correctamente")
    void buscarPorIdTest() {
        // 1. Preparacion (escenario)
        Long idCategoria = 1L;
        Categoria categoriaMock = new Categoria(idCategoria, "Tazas", "Tazas personalizadas", null);

        when(categoriaRepository.findById(idCategoria)).thenReturn(Optional.of(categoriaMock));

        // 2. Ejecucion (When)
        Optional<Categoria> resultado = categoriaService.buscarPorId(idCategoria);

        // 3. Verificacion (Then)
        assertTrue(resultado.isPresent()); // Verificamos que si se encontro la categoria
        assertEquals("Tazas", resultado.get().getNombre()); // Verificamos que los datos no se hayan alterado
        verify(categoriaRepository, times(1)).findById(idCategoria);
    }

    @Test
    @DisplayName("Deberia retornar un Optional vacio si la categoria no existe")
    void buscarPorIdNoExistenteTest() {
        // 1. Preparacion (escenario): el repositorio no encuentra nada
        Long idInexistente = 999L;
        when(categoriaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion (When)
        Optional<Categoria> resultado = categoriaService.buscarPorId(idInexistente);

        // 3. Verificacion (Then)
        assertFalse(resultado.isPresent()); // No debe haber categoria presente
        verify(categoriaRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deberia guardar una categoria correctamente")
    void guardarTest() {
        // 1. Preparacion (escenario)
        Categoria categoria = new Categoria();
        categoria.setNombre("Tazas");
        categoria.setDescripcion("Tazas personalizadas");

        // Simulamos que cuando el repo guarde cualquier categoria, retorne la misma con ID 1
        // Como no queremos usar una base de datos real, le decimos a Mockito:
        // "Cuando alguien llame al metodo .save() del repositorio con cualquier objeto de tipo Categoria
        // (any(Categoria.class))..."
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> {
            Categoria c = invocation.getArgument(0);
            c.setIdCategoria(1L); // le asignamos manualmente el 1
            return c;
        });

        // 2. Ejecucion (When) Llamas al metodo real de tu logica de negocio
        Categoria resultado = categoriaService.guardar(categoria);

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvio un nulo
        assertEquals(1L, resultado.getIdCategoria()); // Compruebas que el ID sea efectivamente el 1 que configuramos
        assertEquals("Tazas", resultado.getNombre()); // Verificas que los datos no se hayan alterado en el camino
        // Verificamos que el repositorio fue llamado exactamente 1 vez. Si tu codigo
        // llamara al repositorio dos veces por error, el test fallaria.
        verify(categoriaRepository, times(1)).save(categoria);
    }

    @Test
    @DisplayName("Deberia eliminar una categoria por id correctamente")
    void eliminarTest() {
        // 1. Preparacion (escenario)
        Long idCategoria = 1L;
        // deleteById no retorna nada (void), Mockito no necesita un "when" para esto

        // 2. Ejecucion (When)
        categoriaService.eliminar(idCategoria);

        // 3. Verificacion (Then)
        // Verificamos que el repositorio fue llamado exactamente 1 vez con el id correcto
        verify(categoriaRepository, times(1)).deleteById(idCategoria);
    }
}
