package com.dreamysips.service_catalogo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dreamysips.service_catalogo.model.Categoria;
import com.dreamysips.service_catalogo.model.Producto;
import com.dreamysips.service_catalogo.repository.ProductoRepository;

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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository; // Simulamos el repositorio de productos

    // OJO: en ProductoService el campo se llama "categoriaRepository" pero su tipo
    // real es CategoriaService (no CategoriaRepository). Por eso aqui mockeamos
    // el Service de Categoria, no su repositorio.
    @Mock
    private CategoriaService categoriaRepository;

    @InjectMocks
    private ProductoService productoService; // Inyectamos los mocks en el servicio real

    @Test
    @DisplayName("Deberia listar todos los productos correctamente")
    void listarTodosTest() {
        // 1. Preparacion (escenario)
        Producto p1 = new Producto(1L, "Taza blanca", 5000, 10, true, null);
        Producto p2 = new Producto(2L, "Polera negra", 12000, 5, true, null);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // 2. Ejecucion (When)
        List<Producto> resultado = productoService.listarTodos();

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificamos que el servicio no devolvio un nulo
        assertEquals(2, resultado.size()); // Comprobamos la cantidad esperada
        verify(productoRepository, times(1)).findAll(); // Verificamos que el repositorio fue llamado exactamente 1 vez
    }

    @Test
    @DisplayName("Deberia buscar un producto por id correctamente")
    void buscarPorIdTest() {
        // 1. Preparacion (escenario)
        Long idProducto = 1L;
        Producto productoMock = new Producto(idProducto, "Taza blanca", 5000, 10, true, null);

        when(productoRepository.findById(idProducto)).thenReturn(Optional.of(productoMock));

        // 2. Ejecucion (When)
        Optional<Producto> resultado = productoService.buscarPorId(idProducto);

        // 3. Verificacion (Then)
        assertTrue(resultado.isPresent()); // Verificamos que si se encontro el producto
        assertEquals("Taza blanca", resultado.get().getDescripcion()); // Verificamos que los datos no se hayan alterado
        verify(productoRepository, times(1)).findById(idProducto);
    }

    @Test
    @DisplayName("Deberia retornar un Optional vacio si el producto no existe")
    void buscarPorIdNoExistenteTest() {
        // 1. Preparacion (escenario): el repositorio no encuentra nada
        Long idInexistente = 999L;
        when(productoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion (When)
        Optional<Producto> resultado = productoService.buscarPorId(idInexistente);

        // 3. Verificacion (Then)
        assertFalse(resultado.isPresent()); // No debe haber producto presente
        verify(productoRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deberia guardar un producto correctamente")
    void guardarTest() {
        // 1. Preparacion (escenario)
        Producto producto = new Producto();
        producto.setDescripcion("Taza blanca");
        producto.setValor(5000);
        producto.setStock(10);
        producto.setEstado(true);

        // Simulamos que cuando el repo guarde cualquier producto, retorne el mismo con ID 1
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            p.setIdProducto(1L); // le asignamos manualmente el 1
            return p;
        });

        // 2. Ejecucion (When)
        Producto resultado = productoService.guardar(producto);

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvio un nulo
        assertEquals(1L, resultado.getIdProducto()); // Compruebas que el ID sea efectivamente el 1 que configuramos
        assertEquals("Taza blanca", resultado.getDescripcion()); // Verificas que los datos no se hayan alterado
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    @DisplayName("Deberia eliminar un producto por id correctamente")
    void eliminarTest() {
        // 1. Preparacion (escenario)
        Long idProducto = 1L;
        // deleteById no retorna nada (void), Mockito no necesita un "when" para esto

        // 2. Ejecucion (When)
        productoService.eliminar(idProducto);

        // 3. Verificacion (Then)
        // Verificamos que el repositorio fue llamado exactamente 1 vez con el id correcto
        verify(productoRepository, times(1)).deleteById(idProducto);
    }

    @Test
    @DisplayName("Deberia guardar un producto con categoria cuando la categoria existe")
    void guardarConCategoriaTest() {
        // 1. Preparacion (escenario)
        Long idCategoria = 1L;
        Categoria categoriaMock = new Categoria(idCategoria, "Tazas", "Tazas personalizadas", null);

        Producto producto = new Producto();
        producto.setDescripcion("Taza blanca");
        producto.setValor(5000);
        producto.setStock(10);
        producto.setEstado(true);

        // Simulamos que el CategoriaService si encuentra la categoria
        when(categoriaRepository.buscarPorId(idCategoria)).thenReturn(Optional.of(categoriaMock));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Ejecucion (When)
        Producto resultado = productoService.guardarConCategoria(idCategoria, producto);

        // 3. Verificacion (Then)
        assertNotNull(resultado); // Verificas que el servicio no devolvio un nulo
        assertNotNull(resultado.getCategoria()); // Verificamos que la categoria fue asignada al producto
        assertEquals(idCategoria, resultado.getCategoria().getIdCategoria()); // Verificamos que sea la correcta
        verify(categoriaRepository, times(1)).buscarPorId(idCategoria);
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    @DisplayName("No deberia guardar un producto con categoria si la categoria no existe")
    void guardarConCategoriaInexistenteTest() {
        // 1. Preparacion (escenario): la categoria no existe
        Long idCategoriaInexistente = 999L;
        Producto producto = new Producto();
        producto.setDescripcion("Taza blanca");

        when(categoriaRepository.buscarPorId(idCategoriaInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion y 3. Verificacion (Then)
        // Verificamos que el servicio lance la excepcion esperada antes de tocar el repositorio de productos
        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> productoService.guardarConCategoria(idCategoriaInexistente, producto));

        assertEquals("Categoria no encontrada ", excepcion.getMessage());
        // El repositorio de productos nunca debio ser invocado porque la validacion corta el flujo antes
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("Deberia vincular un producto existente con una categoria existente")
    void vincularConProductoTest() {
        // 1. Preparacion (escenario)
        Long idCategoria = 1L;
        Long idProducto = 10L;

        Categoria categoriaMock = new Categoria(idCategoria, "Tazas", "Tazas personalizadas", null);
        Producto productoMock = new Producto(idProducto, "Taza blanca", 5000, 10, true, null);

        when(categoriaRepository.buscarPorId(idCategoria)).thenReturn(Optional.of(categoriaMock));
        when(productoRepository.findById(idProducto)).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Ejecucion (When)
        Producto resultado = productoService.vincularConProducto(idCategoria, idProducto);

        // 3. Verificacion (Then)
        assertNotNull(resultado);
        assertNotNull(resultado.getCategoria()); // Verificamos que la categoria quedo vinculada
        assertEquals(idCategoria, resultado.getCategoria().getIdCategoria());
        verify(categoriaRepository, times(1)).buscarPorId(idCategoria);
        verify(productoRepository, times(1)).findById(idProducto);
        verify(productoRepository, times(1)).save(productoMock);
    }

    @Test
    @DisplayName("No deberia vincular si la categoria no existe")
    void vincularConProductoCategoriaInexistenteTest() {
        // 1. Preparacion (escenario): la categoria no existe
        Long idCategoriaInexistente = 999L;
        Long idProducto = 10L;

        when(categoriaRepository.buscarPorId(idCategoriaInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion y 3. Verificacion (Then)
        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> productoService.vincularConProducto(idCategoriaInexistente, idProducto));

        assertEquals("Categoria no encontrada ", excepcion.getMessage());
        // Como la categoria no existe, nunca se debio consultar el producto ni guardar nada
        verify(productoRepository, never()).findById(any());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("No deberia vincular si el producto no existe")
    void vincularConProductoProductoInexistenteTest() {
        // 1. Preparacion (escenario): la categoria si existe, pero el producto no
        Long idCategoria = 1L;
        Long idProductoInexistente = 999L;
        Categoria categoriaMock = new Categoria(idCategoria, "Tazas", "Tazas personalizadas", null);

        when(categoriaRepository.buscarPorId(idCategoria)).thenReturn(Optional.of(categoriaMock));
        when(productoRepository.findById(idProductoInexistente)).thenReturn(Optional.empty());

        // 2. Ejecucion y 3. Verificacion (Then)
        RuntimeException excepcion = assertThrows(RuntimeException.class,
                () -> productoService.vincularConProducto(idCategoria, idProductoInexistente));

        assertEquals("Producto no encontrado con id: " + idProductoInexistente, excepcion.getMessage());
        // El repositorio nunca debio llamar a save() porque la validacion corta el flujo antes
        verify(productoRepository, never()).save(any(Producto.class));
    }
}
